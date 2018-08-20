package hu.zsemberi.experimental.youtubedl

enum class YoutubeDLSaveExtension {
    MP3 {
        override fun toString(): String = "mp3"
    }, WAV {
        override fun toString(): String = "wav"
    }, OGG {
        override fun toString(): String = "opus"
    }, M4A {
        override fun toString(): String = "m4a"
    }, AAC {
        override fun toString(): String = "aac"
    },

    MP4 {
        override fun toString(): String = "mp4"
    }, WEBM {
        override fun toString(): String = "webm"
    }, FLV {
        override fun toString(): String = "flv"
    };

    abstract override fun toString(): String
}