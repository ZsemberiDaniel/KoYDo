package hu.zsemberi.experimental.youtubedl

import java.util.regex.Pattern

/**
 * It's hard to process a console line application output so I tried to provide this method
 * for parsing the download line.
 *
 * Example:\[download]   0.0% of 3.28MiB at 143.97KiB/s ETA 00:23
 *
 * Most of the return fields are strings since I don't know how it writes out the line exactly
 *
 * @return May return null if the provided string is not correct
 */
internal fun String.processDownload(): DownloadProcessResult? =
        // only these strings can be interpreted as download strings
        if (this.startsWith("[download]")) {
            // examples:    [download]   0.0% of 3.28MiB at 143.97KiB/s ETA 00:23
            //              [download]   3.28MiB at 143.97KiB/s
            //              [download]   3.28MiB at 143.97KiB/s (00:23)
            //              [download]   0.0% at 143.97KiB/s ETA 00:23              (***)
            var percent: Double? = null
            var fileSize: String? = null
            var downloadSpeed: String? = null
            var timeLeft: String? = null
            var elapsedTime: String? = null

            // Match for everything with regex
            val progressMatcher = Pattern.compile("(\\S+)\\s+of").matcher(this)
            if (progressMatcher.find()) {
                percent = try {
                    progressMatcher.group(1).dropLast(1).toDouble()
                } catch (e: NumberFormatException) { // couldn't convert it for some reason
                    null
                }
            }

            val speedMatcher = Pattern.compile("(\\S+)\\s+at\\s+(\\S+)").matcher(this)
            if (speedMatcher.find()) {
                // we have to do this because of (***) in examples
                val firstGroup = speedMatcher.group(1)
                if (firstGroup.endsWith("%")) { // then this is a percent
                    percent = try {
                        firstGroup.dropLast(1).toDouble()
                    } catch (e: NumberFormatException) { // couldn't convert it for some reason
                        null
                    }
                } else {
                    fileSize = firstGroup
                }
                downloadSpeed = speedMatcher.group(2)
            }

            val timeLeftMatcher = Pattern.compile("ETA\\s+(\\S+)").matcher(this)
            if (timeLeftMatcher.find()) {
                timeLeft = timeLeftMatcher.group(1)
            }

            val elapsedMatcher = Pattern.compile("\\((\\S+)\\)").matcher(this)
            if (elapsedMatcher.find()) {
                elapsedTime = elapsedMatcher.group(1)
            }

            // This is not a download line if nothing matched
            if (percent == null && fileSize == null && downloadSpeed == null && timeLeft == null) {
                null
            } else {
                DownloadProcessResult(percent, fileSize, downloadSpeed, timeLeft, elapsedTime)
            }
        } else {
            null
        }
data class DownloadProcessResult(val percent: Double?, val fileSize: String?,
                                 val downloadSpeed: String?, val timeLeft: String?,
                                 val elapsedTime: String?)

/**
 * Converts a string which contains youtube dl format data to an object.
 * Example string are
 *
 * 250          webm       audio only DASH audio   76k , opus @ 70k, 2.00MiB
 * 135          mp4        854x480    480p 1359k , avc1.4d401e, 24fps, video only, 23.99MiB
 *
 * @return  May return null if the provided string is not correct
 */
internal fun String.processFormat(): FormatProcessResult? {
    /* Example inputs
     format code  extension  resolution note
     249          webm       audio only DASH audio   58k , opus @ 50k, 1.53MiB
     250          webm       audio only DASH audio   76k , opus @ 70k, 2.00MiB
     135          mp4        854x480    480p 1359k , avc1.4d401e, 24fps, video only, 23.99MiB
     247          webm       1280x720   720p 1536k , vp9, 24fps, video only, 34.86MiB
     */

    // we split by more spaces ot " only " because the above table sucks and only leaves one space after
    // audio only
    val split = this.split(Regex("(\\s\\s+)|( only )"))

    // the first three data in the split is important to us
    if (split.size < 3) return null

    return try {
        FormatProcessResult(split[0].toInt(), split[1], split[2])
    } catch (e: NumberFormatException) { // for some reason cannot convert first to int
        null
    }
}
data class FormatProcessResult(val id: Int, val extension: String, val resolution: String) {
    /**
     * Returns null if this format is audio only. Otherwise it returns the resolution of the video
     *
     * @throws NumberFormatException If there was some problem with the resolution field
     */
    val resolutionOfVideo: Pair<Int, Int>?
        get() {
            if (isAudioOnly()) return null
            val (width, height) = resolution.split("x")

            return try {
                Pair(width.toInt(), height.toInt())
            } catch (e: NumberFormatException) {
                null
            }
        }

    fun isAudioOnly(): Boolean = resolution == "audio"
}

/**
 * Returns a language from a given language format line. Returns null if the line is not valid
 */
internal fun String.processSubtitle(): String? {
    val split = this.split(Regex("(?<!,)(\\s+)"))

    return if (split.isNotEmpty()) {
        split[0]
    } else {
        null
    }
}

/**
 * Makes a watch id from a given youtube id. If it is not possible
 * then it returns null. for example: https://www.youtube.com/watch?v=4bNZK-zgmUc -> 4bNZK-zgmUc
 */
internal fun String.toId(): String? {
    val split = this.split("watch?v=")

    return if (split.size < 2) {
        null
    } else {
        split[1]
    }
}

/**
 * Adds args to this youtube-dl download commands based on what extension we want to save to.
 * For example: MP3 is --extract-audio --audio-format mp3
 */
private fun YoutubeDLCommand.addArgsBasedOnSaveExtension(saveExtension: YoutubeDLSaveExtension): YoutubeDLCommand =
        when (saveExtension) {
            // audios
            YoutubeDLSaveExtension.MP3, YoutubeDLSaveExtension.WAV,
            YoutubeDLSaveExtension.M4A, YoutubeDLSaveExtension.AAC -> { // --extract-audio --audio-format mp3
                this.addArg(YoutubeDLArgList.EXTRACT_AUDIO, null)
                        .addArg(YoutubeDLArgList.AUDIO_FORMAT, saveExtension.toString().toLowerCase())
            }
            YoutubeDLSaveExtension.OGG -> {
                this.addArg(YoutubeDLArgList.EXTRACT_AUDIO, null)
                        .addArg(YoutubeDLArgList.AUDIO_FORMAT, "opus")
            }

            // videos
            YoutubeDLSaveExtension.MP4 -> {
                this
            }
            YoutubeDLSaveExtension.WEBM, YoutubeDLSaveExtension.FLV -> {
                this.addArg(YoutubeDLArgList.FORMAT, saveExtension.toString())
            }
        }

object YoutubeDL {

    /**
     * Where the youtube dl command is at on this computer
     */
    var commandPath: String? = null
        /**
         * Sets the youtube-dl command path of this object.
         *
         * @throws YoutubeDLException If the given path is null or blank
         */
        set(value) {
            if (value == null) {
                throw YoutubeDLException("Command path cannot be set to null!")
            }

            if (value.isBlank()) {
                throw YoutubeDLException("Command path cannot be set to blank!")
            }

            field = value
        }

    /**
     * Saves the given url youtube video to the given path.
     *
     * @param urlOrId           The url of the video for example: https://www.youtube.com/watch?v=4bNZK-zgmUc
     *                          Or the id of the video for example: 4bNZK-zgmUc
     * @param savePath          Where to save the file on your computer. for example: C:/save/here
     * @param saveExtension     In which extension should it save the video file
     * @param customArgs        <i>Optional.</i> If you want to add custom args to the download it can be specified here
     * @param downloadCallback  <i>Optional.</i> This is called after the download starts and provides information about the download
     * @param otherOutputCallback <i>Optional.</i> This is called for every output line that is not connected to the download.
     * @param finishedCallback  <i>Optional.</i> Called when the download finishes. In case of an error it is not called. Rather
     *                          an exception is thrown.
     *
     * @throws YoutubeDLException If there was any error with the download
     *
     * @return The full path where the file was saved example: C:/save/here/4bNZK-zgmUc.mp3
     */
    fun saveToFile(urlOrId: String, savePath: String, saveExtension: YoutubeDLSaveExtension,
                   vararg customArgs: Pair<String, String?> = arrayOf(),
                   downloadCallback: ((downloadProgress: DownloadProcessResult) -> Unit)? = null,
                   otherOutputCallback: ((line: String) -> Unit)? = null,
                   finishedCallback: (() -> Unit)? = null): String {
        val id = urlOrId.toId() ?: urlOrId

        // the youtube command to execute
        // The sequence of additions id very important here because we want the user
        // to be able to override the given commands
        val youtubeCommand = YoutubeDLCommand(id, savePath)
                .addArgsBasedOnSaveExtension(saveExtension)
                .addArg(YoutubeDLArgList.OUTPUT, "$id.${saveExtension.toString().toLowerCase()}")
                .addArg(*customArgs)

        saveToFile(youtubeCommand, downloadCallback, otherOutputCallback, finishedCallback)

        return "$savePath/$id.${saveExtension.toString().toLowerCase()}"
    }

    /**
     * Saves the given url youtube video to the given path.
     *
     * @param properties        What properties to use for downloading video
     * @param downloadCallback  <i>Optional.</i> This is called after the download starts and provides information about the download
     * @param otherOutputCallback <i>Optional.</i> This is called for every output line that is not connected to the download.
     * @param finishedCallback  <i>Optional.</i> Called when the download finishes. In case of an error it is not called. Rather
     *                          an exception is thrown.
     *
     * @throws YoutubeDLException If there was any error with the download
     *
     * @return The full path where the file was saved example: C:/save/here/4bNZK-zgmUc.mp3
     */
    fun saveToFile(properties: YoutubeDLSaveProperties,
                   downloadCallback: ((downloadProgress: DownloadProcessResult) -> Unit)? = null,
                   otherOutputCallback: ((line: String) -> Unit)? = null,
                   finishedCallback: (() -> Unit)? = null): String {
        saveToFile(properties.buildCommand(), downloadCallback, otherOutputCallback, finishedCallback)

        return "${properties.savePath}/${properties.urlOrId}.${properties.extension}"
    }

    /**
     * Private function used for downloading file based on command
     */
    private fun saveToFile(command: YoutubeDLCommand,
                           downloadCallback: ((downloadProgress: DownloadProcessResult) -> Unit)?,
                           otherOutputCallback: ((line: String) -> Unit)?,
                           finishedCallback: (() -> Unit)?) {
        // execute the command: youtube-dl <commands based on extension type> --output %(id)s.%(ext)s <video URL>
        executeCommand(command) {
            // get data about the saving process
            val downloadResult: DownloadProcessResult? = it.processDownload()

            // do callback if necessary
            if (downloadResult != null) {
                if (downloadCallback != null) downloadCallback(downloadResult)
            } else {
                if (otherOutputCallback != null) otherOutputCallback(it)
            }
        }
        if (finishedCallback != null) finishedCallback()
    }



    /**
     * Parses the output of the -F command form youtube-dl. It gives a list of formats.
     *
     * @param urlOrId           The url of the video for example: https://www.youtube.com/watch?v=4bNZK-zgmUc
     *                          Or the id of the video for example: 4bNZK-zgmUc
     *
     * @return A list of possible formats for the given video
     */
    fun getFormatOptions(urlOrId: String): List<FormatProcessResult> {
        val formats = mutableListOf<FormatProcessResult>()
        val id = urlOrId.toId() ?: urlOrId

        // With this variable we indicate that we had the line "format code  extension  resolution note"
        // in the output. It is needed because only after that do we get the format data
        var canBeAccepted = false
        executeCommand(YoutubeDLCommand(id, "/").addArg(YoutubeDLArgList.LIST_FORMATS, null)) {
            // from this point we can accept format lines
            if (it.startsWith("format code")) {
                canBeAccepted = true
                // we can accept format data
            } else if (canBeAccepted) {
                val output = it.processFormat()

                if (output != null) formats.add(output)
            }
        }

        return formats.toList()
    }


    /**
     * Returns the options available for subtitles. It returns both the automatic and the
     * normal subtitles
     *
     * @param urlOrId           The url of the video for example: https://www.youtube.com/watch?v=4bNZK-zgmUc
     *                          Or the id of the video for example: 4bNZK-zgmUc
     */
    fun getSubtitleOptions(urlOrId: String): HashSet<String> {
        // list the subtitles with this command

        // if it has none available then it looks like
        // VouaAz5mQAs has no automatic captions
        // VouaAz5mQAs has no subtitles

        // if it has available then
        // Available automatic captions for f7KSfjv4Oq0:
        // Language formats
        // yi       vtt, ttml
        // (...)
        // Available subtitles for f7KSfjv4Oq0:
        // Language formats
        // ja       vtt, ttml
        // (..)
        val id = urlOrId.toId() ?: urlOrId

        val output = hashSetOf<String>()
        // Indicates whether we have entered the subtitle mode
        // If entered all the lines are parsed as subtitles
        var parseSubtitleMode = false

        executeCommand(YoutubeDLCommand(id, "/").addArg(YoutubeDLArgList.LIST_SUBS, null)) {
            // we can parse all the lines after these ones
            if (it.startsWith("Available automatic captions") ||
                    it.startsWith("Available subtitles")) {
                parseSubtitleMode = true
                // from this point on there will be no subtitles
            } else if (it.endsWith("has no subtitles")) {
                parseSubtitleMode = false
                // we can parse these lines
            } else if (parseSubtitleMode && !it.startsWith("Language formats")) {
                val subt = it.processSubtitle()

                if (subt != null)
                    output.add(subt)
            }
        }

        return output
    }


    /**
     * Executes the given command in a blocking way
     *
     * @param command What command to execute
     * @param outputCallback <i>Optional.</i> The callback to call for the output of the command
     *
     * @throws YoutubeDLException If the command path has not been set. Or if there was an error with the download
     */
    private fun executeCommand(command: YoutubeDLCommand, outputCallback: ((newLine: String) -> Unit)? = null) {
        if (commandPath.isNullOrBlank())
            throw YoutubeDLException("You have not set the path to the youtube-dl command! Use YoutubeDL.commandPath = \"path\"")

        command.executeCommand(commandPath!!) {
            if (outputCallback != null) outputCallback(it)
        }.waitFor()
    }
}