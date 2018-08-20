package hu.zsemberi.experimental.youtubedl

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YoutubeDLTest() {

    private val savePath = "${System.getProperty("user.home")}\\web\\spotToYoutube"

    init {
        YoutubeDL.commandPath =  "C:\\youtube-dl\\youtube-dl.exe"
    }

    @Test
    fun `basic mp3 test`() {
        val file = File("$savePath\\kHLHSlExFis.mp3")
        if (file.exists())
            file.delete()

        YoutubeDLCommand("kHLHSlExFis", savePath)
                .addArg(YoutubeDLArgList.EXTRACT_AUDIO, null)
                .addArg(YoutubeDLArgList.AUDIO_FORMAT, "mp3")
                .executeCommand(YoutubeDL.commandPath!!).waitFor()

        assert(File("$savePath\\kHLHSlExFis.mp3").exists())
    }

    @Test
    fun `YoutubeDL mp3 test`() {
        val file = File("$savePath\\kHLHSlExFis.mp3")
        if (file.exists())
            file.delete()

        YoutubeDL.saveToFile(urlOrId = "kHLHSlExFis", savePath = savePath, saveExtension = YoutubeDLSaveExtension.MP3)

        assert(File("$savePath\\kHLHSlExFis.mp3").exists())
    }

    @Test
    fun `YoutubeDL webm test`() {
        val file = File("$savePath\\kHLHSlExFis.webm")
        if (file.exists())
            file.delete()

        YoutubeDL.saveToFile(urlOrId = "kHLHSlExFis", savePath = savePath, saveExtension = YoutubeDLSaveExtension.WEBM)

        assert(File("$savePath\\kHLHSlExFis.webm").exists())
    }

    @Test
    fun `YoutubeDL webm with properties`() {
        val file = File("$savePath\\kHLHSlExFis.webm")
        if (file.exists()) file.delete()

        YoutubeDL.saveToFile(properties = YoutubeDLSaveProperties(
                urlOrId = "kHLHSlExFis", savePath = savePath, videoExtension = YoutubeDLSaveExtension.WEBM,
                audioExtension = YoutubeDLSaveExtension.WEBM
        ))

        assert(File("$savePath\\kHLHSlExFis.webm").exists())
    }

    @Test
    fun `YoutubeDL MP4 M4A with properties`() {
        val file = File("$savePath\\bhxhNIQBKJI.mp4")
        if (file.exists()) file.delete()

        YoutubeDL.saveToFile(properties = YoutubeDLSaveProperties(
                urlOrId = "bhxhNIQBKJI", savePath = savePath, videoExtension = YoutubeDLSaveExtension.MP4,
                audioExtension = YoutubeDLSaveExtension.M4A
        ))

        assert(File("$savePath\\bhxhNIQBKJI.mp4").exists())
    }

    @Test
    fun `YoutubeDL format options`() {
        val formatOptions = YoutubeDL.getFormatOptions("kHLHSlExFis")

        assert(formatOptions.isNotEmpty())
    }

    @Test
    fun `YoutubeDL subtitle options`() {
        val subtitleOptions = YoutubeDL.getSubtitleOptions("kHLHSlExFis")

        assert(subtitleOptions.isNotEmpty())
    }

    @Test
    fun `YoutubeDL all options enabled from properties`() {
        var file = File("$savePath\\kHLHSlExFis.mp4")
        if (file.exists()) file.delete()

        file = File("$savePath\\kHLHSlExFis.jpg")
        if (file.exists()) file.delete()

        val subtitleOptions = YoutubeDL.getSubtitleOptions("kHLHSlExFis")

        YoutubeDLSaveProperties(
                savePath = savePath,
                urlOrId = "kHLHSlExFis",
                videoExtension = YoutubeDLSaveExtension.MP4,
                audioExtension = YoutubeDLSaveExtension.M4A,
                writeThumbnailFile = true,
                embedThumbnail = true,
                addMetadata = true,
                subLanguages = listOf(subtitleOptions.elementAt(0)),
                embedSub = true,
                writeSubFile = true,
                writeAutoSubFile = true
        ).buildCommand().executeCommand(YoutubeDL.commandPath!!).waitFor()

        YoutubeDLCommand("", "save\\path")
                .addArg(YoutubeDLArgList.ADD_METADATA, null)
                .addArg(YoutubeDLArgList.WRITE_SUB to null, YoutubeDLArgList.SUB_LANG to "en,es")
                .addArg(YoutubeDLArgList.BUFFER_SIZE, "2048")
                .executeCommand(YoutubeDL.commandPath!!) {
                    println("YOUTUBE-DL OUTPUT: $it")
                }.waitFor()

        assert(File("$savePath\\kHLHSlExFis.mp4").exists())
        assert(File("$savePath\\kHLHSlExFis.jpg").exists())
    }
}