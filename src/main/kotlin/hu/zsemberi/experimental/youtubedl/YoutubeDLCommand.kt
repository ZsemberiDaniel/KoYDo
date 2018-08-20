package hu.zsemberi.experimental.youtubedl

import kotlinx.coroutines.experimental.launch
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

data class YoutubeDLCommand(val urlOrId: String, val savePath: String) {

    val id: String = urlOrId.toId() ?: urlOrId

    /**
     * List of args for the youtube-dl command
     */
    private val argList: MutableMap<String, String> = mutableMapOf()

    /**
     * @return Whether this command contains the given argument
     */
    fun containsArg(arg: String) = argList.keys.contains(arg)

    /**
     * Adds an arg to this youtubeDLCommand
     */
    fun addArg(arg: String, value: String?): YoutubeDLCommand {
        argList[arg] = value ?: ""

        return this
    }

    /**
     * Add args to this youtubeDLCommands
     */
    fun addArg(vararg args: Map.Entry<String, String?>): YoutubeDLCommand {
        args.forEach {
            argList[it.key] = it.value ?: ""
        }

        return this
    }

    /**
     * Add args to this youtubeDLCommands
     */
    fun addArg(vararg args: Pair<String, String?>): YoutubeDLCommand {
        args.forEach {
            argList[it.first] = it.second ?: ""
        }

        return this
    }

    /**
     * Execute a command for the command line from this YoutubeDLCommand
     *
     * @param commandPath Where the youtube-dl command's exe is on this computer
     * @param updateCallBack Calls this function for every line the command writes out.
     *                       If there was en error this will be ERROR: message
     *
     * @throws YoutubeDLException If the given id in this command is blank. Or if there was an error with the download
     */
    fun executeCommand(commandPath: String, updateCallBack: ((newLine: String) -> Unit)? = null): Process {
        if (id.isBlank()) {
            throw YoutubeDLException("No ID provided!")
        }

        // Add basic naming scheme if nothing was provided
        if (!argList.keys.contains(YoutubeDLArgList.OUTPUT)) {
            addArg(YoutubeDLArgList.OUTPUT, "$id.%(ext)s")
        }

        // Build a command line process for youtube-dl
        val processBuilder = ProcessBuilder(mutableListOf(
                commandPath, // command itself
                *argList.flatMap { entry -> // mapping args
                    val mutableList = mutableListOf(
                            // if this key is a length of 1 we only need on dash
                            if (entry.key.length == 1) { "-${entry.key}" } else { "--${entry.key}" }
                    )

                    // only add parameter if there is one
                    if (entry.value.isNotBlank()) {
                        mutableList.add(entry.value)
                    }

                    mutableList
                }.toTypedArray(),
                id // id at end
        )).directory(File(savePath)) // do it in the given directory so it saves correctly
        val process = processBuilder.start()


        // we need to read the lines even if there is no callback otherwise it won't work
        // launch one for non-error lines
        launch {
            // other lines
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            var newLine: String?

            // process could still spit out lines OR there are still lines to be read
            do {
                newLine = bufferedReader.readLine()

                if (newLine != null) {
                    throw YoutubeDLException("There was a problem with the download: $newLine")
                }
            } while (process.isAlive || newLine != null)
        }

        // launch one for error lines
        launch {
            // error lines
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            var newErrorLine: String?

            do {
                newErrorLine = errorReader.readLine()

                if (newErrorLine != null && updateCallBack != null) {
                    updateCallBack(newErrorLine)
                }
                // process could still spit out lines OR there are still lines to be read
            } while (process.isAlive || newErrorLine != null)
        }

        return process
    }

    private fun buildCommandText(): String = mutableListOf(
            "youtube-dl", // command itself
            argList.map { entry -> // mapping args
                "--${entry.key}${if (entry.value.isNotBlank()) { " ${entry.value}" } else { "" }}"
            }.joinToString(" "),
            id // id at end
    ).joinToString(" ")

    override fun toString(): String = buildCommandText()
}