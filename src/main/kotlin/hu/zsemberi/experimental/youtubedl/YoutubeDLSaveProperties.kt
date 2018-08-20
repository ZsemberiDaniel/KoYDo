package hu.zsemberi.experimental.youtubedl

import java.util.logging.Level
import java.util.logging.Logger

/**
 * A class that makes it easier to construct the command. You don't need to learn all the commands.
 * The savePath and urlOrId fields are NOT optional while the others are. If nothing else is provided then
 * the download will be an mp4 video combined with an m4a audio.
 *
 * If you want to specify the extension I recommend using YoutubeDL.getFormatOptions() and choosing from there
 * but you can specify both the video and the audio extension. If only audio is wanted then you can set audioOnly to true
 * and then videoExtension will be ignored.
 *
 * If an outputName is not specified then a default "$urlOrId.$extension" will be used.
 *
 * The name of the separate files will be the same as the name of the youtube download file.
 *
 * For subtitle options you can use YoutubeDL.getSubtitleOptions(). But of course you can provide one by yourself as well.
 */
data class YoutubeDLSaveProperties(
        /**
         * Where to save the video/audio
         */
        val savePath: String,

        /**
         * Id or url must be given.
         */
        var urlOrId: String,

        /**
         * A format for the video. If this is provided it is the preferred one.
         * If provided videoExtension, audioExtension and audioOnly does not have to be set.
         */
        val format: FormatProcessResult? = null,
        /**
         * What extension to use if saving video. Beware that it may not be available for the video.
         * To check that use YoutubeDL.getFormatOptions()
         */
        val videoExtension: YoutubeDLSaveExtension? = null,
        /**
         * What extension to use if saving audio/video. Beware that it may not be available for the video.
         * To check that use YoutubeDL.getFormatOptions()
         */
        val audioExtension: YoutubeDLSaveExtension? = null,
        /**
         * Indicate that the video is not needed only the audio. If set to true videoExtension will be ignored
         */
        val audioOnly: Boolean = false,

        /**
         * Defines the name of the output file after save happens. It also needs an extension
         */
        val outputName: String? = null,

        /**
         * With this a youtube user can be authenticated. First element is name second is password.
         */
        val authenticate: Pair<String, String>? = null,

        /**
         * If true the video's thumbnail will be written in a separate file
         */
        val writeThumbnailFile: Boolean = false,
        /**
         * If true the video's thumbnail will be embedded to the file
         */
        val embedThumbnail: Boolean = false,

        /**
         * If true the video's subtitle will be written to a separate file
         */
        val writeSubFile: Boolean = false,
        /**
         * If true the video's automatic subtitle will be written to a separate file
         */
        val writeAutoSubFile: Boolean = false,
        /**
         * If true the video's subtitle will be embedded in the video
         */
        val embedSub: Boolean = false,
        /**
         * Specify which languages are needed for subtitles
         */
        val subLanguages: List<String> = listOf(),

        /**
         * If true metadata will be added to the file
         */
        val addMetadata: Boolean = false,

        /**
         * If the above properties are not enough then you can use this to add your own properties.
         * If it matches with one of the one you specified above then that will be preferred.
         */
        val customProperties: List<Pair<String, String?>> = listOf()
) {

    private val logger: Logger = Logger.getLogger(YoutubeDLSaveProperties::class.simpleName)

    init {
        // make it only the id
        urlOrId = urlOrId.toId() ?: urlOrId
    }

    val extension: String
        get() = when {
            // format was provided -> use that
            format != null -> format.extension
            // audio only download
            audioOnly -> audioExtension.toString()
            // video and audio download
            (videoExtension != null) -> videoExtension.toString()
            else -> "mp4"
        }

    /**
     * Build a YoutubeDLCommand from these properties. That command will download the video from the
     * given url/id.
     *
     * @throws YoutubeDLException Might throw an exception if the id is not set and the url is not valid.
     */
    fun buildCommand(): YoutubeDLCommand {
        // at this point url or id is for sure containing the id

        // The command we will be adding to
        return YoutubeDLCommand(urlOrId, savePath).also {
            // ** ** ** PROCESSING EXTENSIONS ** ** **
            when {
                // format was provided -> use that
                format != null -> it.addArg(YoutubeDLArgList.FORMAT, format.id.toString())
                // audio only download
                audioOnly -> it.addArg(YoutubeDLArgList.EXTRACT_AUDIO, null)
                               .addArg(YoutubeDLArgList.AUDIO_FORMAT,
                                    // the default audio extension is mp3
                                    (audioExtension ?: YoutubeDLSaveExtension.MP3).toString()
                                )
                else -> { // video and audio download
                    it.addArg(YoutubeDLArgList.FORMAT,
                            "bestvideo[ext=${(videoExtension ?: YoutubeDLSaveExtension.MP4)}]+" +
                                   "bestaudio[ext=${(audioExtension ?: YoutubeDLSaveExtension.M4A)}]"
                    )
                }
            }

            // ** ** ** NAMING FILE ** ** **
            if (outputName != null) {
                it.addArg(YoutubeDLArgList.OUTPUT, outputName)
            } else {
                it.addArg(YoutubeDLArgList.OUTPUT, "$urlOrId.$extension")
            }

            // ** ** ** AUTHENTICATE ** ** **
            if (authenticate != null) {
                it.addArg(YoutubeDLArgList.USERNAME, authenticate.first)
                it.addArg(YoutubeDLArgList.PASSWORD, authenticate.second)
            }

            // ** ** ** THUMBNAIL ** ** **
            if (writeThumbnailFile) {
                it.addArg(YoutubeDLArgList.WRITE_THUMBNAIL, null)
            }
            if (embedThumbnail) {
                it.addArg(YoutubeDLArgList.EMBED_THUMBNAIL, null)
            }

            // ** ** ** SUBTITLES ** ** **
            if (writeSubFile) {
                it.addArg(YoutubeDLArgList.WRITE_SUB, null)
            }
            if (writeAutoSubFile) {
                it.addArg(YoutubeDLArgList.WRITE_AUTO_SUB, null)
            }
            if (embedSub) {
                it.addArg(YoutubeDLArgList.EMBED_SUBS, null)
            }
            if (subLanguages.isNotEmpty()) {
                it.addArg(YoutubeDLArgList.SUB_LANG, subLanguages.joinToString(","))
            }

            // inform user what is up dog
            if (embedSub && (writeSubFile || writeAutoSubFile))
                logger.log(Level.WARNING, "If embed sub is enabled then the separate subtitles files will be deleted! " +
                        "If you want to keep the separate subtitles as well add keep-video (from arg list) to the customProperties. " +
                        "But be aware that if youtube dl needs to merge the video and audio file then both will be kept on disk " +
                        "that is why it's not kept on by default.")

            // ** ** ** METADATA ** ** **
            if (addMetadata) {
                it.addArg(YoutubeDLArgList.ADD_METADATA, null)
            }

            // ** ** ** CUSTOM ** ** **
            if (customProperties.isNotEmpty()) {
                for (arg in customProperties) {
                    // prefer the ones above
                    if (!it.containsArg(arg.first)) {
                        it.addArg(arg)
                    }
                }
            }
        }
    }
}