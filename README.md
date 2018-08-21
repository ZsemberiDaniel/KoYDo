# KoYDo
First af all I'm aware that this is the stupidest name you have ever read for a library but I have no idea what to call it. Calling it Kotlin Youtube DL is just boring. I'm open to ideas though.

This is an implementation of youtube-dl command line application in Kotlin. It makes working with its's input and output much easier.


Note that this library uses **Kotlin Coroutines** which are still **experimental**. Because of that this library is in an experimental package.

# <a name="setup"></a>Setup
You need to download [youtube-dl](https://rg3.github.io/youtube-dl/download.html).

If you want to download mp3 as well get [ffmpeg](https://www.ffmpeg.org/download.html) as well and put that in the same directory.

If you want to embed metadata (artist, title etc.) to your files you need [AtomicParsley](https://sourceforge.net/projects/atomicparsley/files/) in the same directory as well.

![Full library setup](https://i.imgur.com/7xIPc9x.png)

*A full setup*

### Setup in code
You need to specify the absolute path to the youtube-dl.exe **at the start of your application** via

```kotlin
    YoutubeDL.commandPath = "C:\\path\\to\\youtube-dl.exe"
```

If it is not set you won't be able tu use convenient methods but some methods do work.

# How to use
You don't need to be familiar with the youtube-dl.exe itself because if you set up the path to your exe correctly
this library provide convenient methods to execute commands.

### Definitions
**This part is really important to truly understand this documentation without any confusion!**
* **Youtube url**: The url of the youtube video. Example: `https://www.youtube.com/watch?v=bhxhNIQBKJI`
* **Youtube id**: The id of the youtube video. So everything after `watch?v=`. For the one above: `bhxhNIQBKJI`

### Convenience methods
There are two methods that save to files. The first one is

```kotlin
    YoutubeDL.saveToFile(urlOrId, savePath, saveExtension, customArgs,
                         downloadCallback, otherOutputCallback, finishedCallback) {...}
```

For the customArgs you can use the [YoutubeDLArgList](#args) for help.
The last four parameters are optional. The description of this method can be found in the javadoc.


The other one requires a `YoutubeDLSaveProperties` object which describes the first four parameters of the function above.
Refer to the [YoutubeDLSaveProperties](#YoutubeDLSaveProperties) section for further clarification of this object.

```kotlin
    YoutubeDL.saveToFile(properties, downloadCallback, otherOutputCallback, finishedCallback)
```

These methods may throw a `YoutubeDLException` if the output of youtube-dl had an error as well. They also return the absolute path of the saved file.

#### <a name="YoutubeDLSaveProperties"></a>YoutubeDLSaveProperties
It has never been easier to define console parameters! This class does all the heavy lifting for you and contains the most common parameters for youtube-dl.
Even if the stupid developer did not think that a parameter was necessary you can add them yourself next to the already defined ones.

There are two parameters that are mandatory: urlOrId (which video to download) and savePath (where to save the video).
If you want to download the video only with these parameters you can do so... but there are so many other parameters to choose from, so why would you?

All of the parameters can be found in the javadoc but there are some things that need clarification:

##### Format of video
There are three ways to define the format of the video and here they are in order: (higher it is more preferred it is by the code, so if you use option 1 and option 2 in the same property class option 1 will be chosen)

1. Providing a `FormatProcessResult` from `YoutubeDL.getFormatOptions(..)`. (Refer to [Format options](#FormatOptions))
With this you can provide one format that will surely be available. Note: if you want both a video and an audio layer then this is not the recommended.
2. Providing an `audioExtension` and setting `audioOnly` to `true`. If you have ffmpeg installed there should be no problem converting
to any audio extension with this. The `videoExtension` will be ignored.
3. Providing both a `videoExtension` and an `audioExtension`. If both are provided they will be both downloaded separately and then
combined to one file. (Unless specified otherwise by the user) Note that these extensions may not be available on youtube so you first need to check via `YoutubeDL.getFormatOptions(..)`.

##### Name of output

If the `outputName` is not set a default `"$id"` will be provided. Don't set the extension here, let the library handle that for you because it may disrupt the conversions.

##### Thumbnails

If both `writeThumbnailFile` and `embedThumbnail` are set to true then the separate thumbnail file will not be deleted from disk.

##### Subtitles

If both `writeSubFile` or `writeAutoSubFile` and `embedSub` is set to true at the end the separate subtitle files **WILL BE** deleted from the disk. The library warns you of this.

Also it might be possible that a language is not available for the current video. You can [check that](#subtitleOptions) with `YoutubeDL.getSubtitleOptions(..)`.

##### Metadata

For adding metadata you need [AtomicParsley](#setup).

##### Custom parameters

You can specify as many as you want but if it matches with one of the one you specified above (with fields) then that will be preferred.

For easier parameter addition you can use `YoutubeDLArgList` where you can [find](#args) all the parameters with descriptions.

If the parameter does not need an input then the second part of the pair should be null.

##### Example

An example usage of the `YoutubeDlProperties` instantiation:

```kotlin
    YoutubeDLSaveProperties(
        savePath = "\\save\\here\\my\\boy",
        urlOrId = "kHLHSlExFis",
        videoExtension = YoutubeDLSaveExtension.MP4,
        audioExtension = YoutubeDLSaveExtension.M4A,
        writeThumbnailFile = true,
        embedThumbnail = true,
        addMetadata = true,
        subLanguages = listOf("en"),
        embedSub = false, // this is false so the subtitle files won't be deleted from disk
        writeSubFile = true,
        writeAutoSubFile = true
    )
```

Note that before this we should check whether both the extensions and the subtitles are available. For example:

```kotlin
    ...
    val subtitleOptions = YoutubeDL.getSubtitleOptions("kHLHSlExFis")
    if (!subtitleOptions.contains("en")) return "Problem with download!"

    val videoOptions = YoutubeDL.getFormatOptions("kHLHSlExFis").map { it.extension }.toHashSet()
    if (!videoOptions.contains("mp4")) return "Problem with download!"
    if (!videoOptions.contains("m4a")) return "Problem with download!"
    ...
```

### YoutubeDLCommand

If you want full control over your java `Process` which executes the youtube-dl command in command line you can use the `YoutubeDLCommand`. It needs a youtube url or id and a save path.

After that you can also add parameters to your command. I recommend using `YoutubeDLArgList` so they are valid parameters for sure. If the parameter does not need an input then the second parameter should be null.

If a parameter is added twice the second one will be preferred.

After adding all your parameters you can execute your command via

```kotlin
    command.executeCommand(commandPath, updateCallBack)
```

the commandPath is the directory where the youtube-dl.exe is. With updateCallbacks you can get the output lines of the youtube-dl command. If there was an error with the youtube-dl command then a `YoutubeDLException` will be thrown.

Note that this function returns a `Process` that has been started.

##### Converting from YoutubeDLSaveProperties

You can convert from `YoutubeDLSaveProperties` to `YoutubeCommend` with the simple function but it does not work the other way around

```kotlin
    properties.buildCommand()
```

##### Example of YoutubeDLCommand

```kotlin
    YoutubeDLCommand("", "save\\path")
        .addArg(YoutubeDLArgList.ADD_METADATA, null)
        .addArg(YoutubeDLArgList.WRITE_SUB to null, YoutubeDLArgList.SUB_LANG to "en,es") // adding more args
        .addArg(YoutubeDLArgList.BUFFER_SIZE, "2048")
        .executeCommand(YoutubeDL.commandPath!!) { // executing this youtube dl command
            println("YOUTUBE-DL OUTPUT: $it") // printing the output
        .waitFor() // waiting for the Process to finish
```

### <a name="FormatOptions"></a>Format options

If you want to get information about which formats are available for a given video you can do that via

```kotlin
    YoutubeDL.getFormatOptions(urlOrId): List<FormatProcessResult>()
```

It returns a list of object which contain the id of the format (can be passed to parameter -format), the name of the format and if the format is not audio only then the resolution of the format.

### <a name="subtitleOptions"></a>Subtitle options

If you want to get information about what kind of languages are available for the current video (both auto generated and written by humans) you can use

```kotlin
    YoutubeDL.getSubtitleOptions(urlOrId): HashSet<String>()
```

### <a name="args"></a>YoutubeDLArgList

This class provides all the parameters you can use with youtube-dl. All of them also have a description "stolen" straight from the GitHub of youtube-dl. This way you don't have to swap between my project and the youtube-dl documentation.
