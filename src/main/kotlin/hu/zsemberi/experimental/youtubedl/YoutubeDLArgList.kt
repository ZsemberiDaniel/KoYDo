package hu.zsemberi.experimental.youtubedl

object YoutubeDLArgList {
    /**
     * With arg: NUMBER
     * Playlist video to start at (default is 1)
     */
    const val PLAYLIST_START = "playlist-start"

    /**
     * With arg: NUMBER
     * Playlist video to end at (default is last)
     */
    const val PLAYLIST_END = "playlist-end"

    /**
     * With arg: ITEM_SPEC
     * Playlist video items to download. Specify
     * indices of the videos in the playlist
     * separated by commas like: "--playlist-items
     * 1,2,5,8" if you want to download videos
     * indexed 1, 2, 5, 8 in the playlist. You can
     * specify range: "--playlist-items
     * 1-3,7,10-13", it will download the videos
     * at index 1, 2, 3, 7, 10, 11, 12 and 13.
     */
    const val PLAYLIST_ITEMS = "playlist-items"

    /**
     * With arg: REGEX
     * Download only matching titles (regex or
     * caseless sub-string)
     */
    const val MATCH_TITLE = "match-title"

    /**
     * With arg: REGEX
     * Skip download for matching titles (regex or
     * caseless sub-string)
     */
    const val REJECT_TITLE = "reject-title"

    /**
     * With arg: NUMBER
     * Abort after downloading NUMBER files
     */
    const val MAX_DOWNLOADS = "max-downloads"

    /**
     * With arg: SIZE
     * Do not download any videos smaller than
     * SIZE (e.g. 50k or 44.6m)
     */
    const val MIN_FILESIZE = "min-filesize"

    /**
     * With arg: SIZE
     * Do not download any videos larger than SIZE
     * (e.g. 50k or 44.6m)
     */
    const val MAX_FILESIZE = "max-filesize"

    /**
     * With arg: DATE
     * Download only videos uploaded in this date
     */
    const val DATE = "date"

    /**
     * With arg: DATE
     * Download only videos uploaded on or before
     * this date (i.e. inclusive)
     */
    const val DATEBEFORE = "datebefore"

    /**
     * With arg: DATE
     * Download only videos uploaded on or after
     * this date (i.e. inclusive)
     */
    const val DATEAFTER = "dateafter"

    /**
     * With arg: COUNT
     * Do not download any videos with less than
     * COUNT views
     */
    const val MIN_VIEWS = "min-views"

    /**
     * With arg: COUNT
     * Do not download any videos with more than
     * COUNT views
     */
    const val MAX_VIEWS = "max-views"

    /**
     * With arg: FILTER
     * Generic video filter. Specify any key (see
     * the "OUTPUT TEMPLATE" for a list of
     * available keys) to match if the key is
     * present, !key to check if the key is not
     * present, key > NUMBER (like "comment_count
     * > 12", also works with >=, <, <=, !=, =) to
     * compare against a number, key = 'LITERAL'
     * (like "uploader = 'Mike Smith'", also works
     * with !=) to match against a string literal
     * and & to require multiple matches. Values
     * which are not known are excluded unless you
     * put a question mark (?) after the operator.
     * For example, to only match videos that have
     * been liked more than 100 times and disliked
     * less than 50 times (or the dislike
     * functionality is not available at the given
     * service), but who also have a description,
     * use --match-filter "like_count > 100 &
     * dislike_count <? 50 & description" .
     */
    const val MATCH_FILTER = "match-filter"

    /**
     * Download only the video, if the URL refers
     * to a video and a playlist.
     */
    const val NO_PLAYLIST = "no-playlist"

    /**
     * Download the playlist, if the URL refers to
     * a video and a playlist.
     */
    const val YES_PLAYLIST = "yes-playlist"

    /**
     * With arg: YEARS
     * Download only videos suitable for the given
     * age
     */
    const val AGE_LIMIT = "age-limit"

    /**
     * With arg: FILE
     * Download only videos not listed in the
     * archive file. Record the IDs of all
     * downloaded videos in it.
     */
    const val DOWNLOAD_ARCHIVE = "download-archive"

    /**
     * With arg: RATE
     * Maximum download rate in bytes per second
     * (e.g. 50K or 4.2M)
     */
    const val LIMIT_RATE = "limit-rate"

    /**
     * With arg: RETRIES
     * Number of retries (default is 10), or
     * "infinite".
     */
    const val RETRIES = "retries"

    /**
     * With arg: RETRIES
     * Number of retries for a fragment (default
     * is 10), or "infinite" (DASH, hlsnative and
     * ISM)
     */
    const val FRAGMENT_RETRIES = "fragment-retries"

    /**
     * Skip unavailable fragments (DASH, hlsnative
     * and ISM)
     */
    const val SKIP_UNAVAILABLE_FRAGMENTS = "skip-unavailable-fragments"

    /**
     * Abort downloading when some fragment is not
     * available
     */
    const val ABORT_ON_UNAVAILABLE_FRAGMENT = "abort-on-unavailable-fragment"

    /**
     * Keep downloaded fragments on disk after
     * downloading is finished; fragments are
     * erased by default
     */
    const val KEEP_FRAGMENTS = "keep-fragments"

    /**
     * With arg: SIZE
     * Size of download buffer (e.g. 1024 or 16K)
     * (default is 1024)
     */
    const val BUFFER_SIZE = "buffer-size"

    /**
     * Do not automatically adjust the buffer
     * size. By default, the buffer size is
     * automatically resized from an initial value
     * of SIZE.
     */
    const val NO_RESIZE_BUFFER = "no-resize-buffer"

    /**
     * With arg: SIZE
     * Size of a chunk for chunk-based HTTP
     * downloading (e.g. 10485760 or 10M) (default
     * is disabled). May be useful for bypassing
     * bandwidth throttling imposed by a webserver
     * (experimental)
     */
    const val HTTP_CHUNK_SIZE = "http-chunk-size"

    /**
     * Download playlist videos in reverse order
     */
    const val PLAYLIST_REVERSE = "playlist-reverse"

    /**
     * Download playlist videos in random order
     */
    const val PLAYLIST_RANDOM = "playlist-random"

    /**
     * Set file xattribute ytdl.filesize with
     * expected file size
     */
    const val XATTR_SET_FILESIZE = "xattr-set-filesize"

    /**
     * Use the native HLS downloader instead of
     * ffmpeg
     */
    const val HLS_PREFER_NATIVE = "hls-prefer-native"

    /**
     * Use ffmpeg instead of the native HLS
     * downloader
     */
    const val HLS_PREFER_FFMPEG = "hls-prefer-ffmpeg"

    /**
     * Use the mpegts container for HLS videos,
     * allowing to play the video while
     * downloading (some players may not be able
     * to play it)
     */
    const val HLS_USE_MPEGTS = "hls-use-mpegts"

    /**
     * With arg: COMMAND
     * Use the specified external downloader.
     * Currently supports
     * aria2c,avconv,axel,curl,ffmpeg,httpie,wget
     */
    const val EXTERNAL_DOWNLOADER = "external-downloader"

    /**
     * With arg: ARGS
     * Give these arguments to the external
     * downloader
     */
    const val EXTERNAL_DOWNLOADER_ARGS = "external-downloader-args"

    /**
     * With arg: FILE
     * File containing URLs to download ('-' for
     * stdin), one URL per line. Lines starting
     * with '#', ';' or ']' are considered as
     * comments and ignored.
     */
    const val BATCH_FILE = "batch-file"

    /**
     * Use only video ID in file name
     */
    const val ID = "id"

    /**
     * With arg: TEMPLATE
     * Output filename template, see the "OUTPUT
     * TEMPLATE" for all the info
     */
    const val OUTPUT = "output"

    /**
     * With arg: NUMBER
     * Specify the start value for %(autonumber)s
     * (default is 1)
     */
    const val AUTONUMBER_START = "autonumber-start"

    /**
     * Restrict filenames to only ASCII
     * characters, and avoid "&" and spaces in
     * filenames
     */
    const val RESTRICT_FILENAMES = "restrict-filenames"

    /**
     * Do not overwrite files
     */
    const val NO_OVERWRITES = "no-overwrites"

    /**
     * Force resume of partially downloaded files.
     * By default, youtube-dl will resume
     * downloads if possible.
     */
    const val CONTINUE = "continue"

    /**
     * Do not resume partially downloaded files
     * (restart from beginning)
     */
    const val NO_CONTINUE = "no-continue"

    /**
     * Do not use .part files - write directly
     * into output file
     */
    const val NO_PART = "no-part"

    /**
     * Do not use the Last-modified header to set
     * the file modification time
     */
    const val NO_MTIME = "no-mtime"

    /**
     * Write video description to a .description
     * file
     */
    const val WRITE_DESCRIPTION = "write-description"

    /**
     * Write video metadata to a .info.json file
     */
    const val WRITE_INFO_JSON = "write-info-json"

    /**
     * Write video annotations to a
     * .annotations.xml file
     */
    const val WRITE_ANNOTATIONS = "write-annotations"

    /**
     * With arg: FILE
     * JSON file containing the video information
     * (created with the "--write-info-json"
     * option)
     */
    const val LOAD_INFO_JSON = "load-info-json"

    /**
     * With arg: FILE
     * File to read cookies from and dump cookie
     * jar in
     */
    const val COOKIES = "cookies"

    /**
     * With arg: DIR
     * Location in the filesystem where youtube-dl
     * can store some downloaded information
     * permanently. By default
     * $XDG_CACHE_HOME/youtube-dl or
     * ~/.cache/youtube-dl . At the moment, only
     * YouTube player files (for videos with
     * obfuscated signatures) are cached, but that
     * may change.
     */
    const val CACHE_DIR = "cache-dir"

    /**
     * Disable filesystem caching
     */
    const val NO_CACHE_DIR = "no-cache-dir"

    /**
     * Delete all filesystem cache files
     */
    const val RM_CACHE_DIR = "rm-cache-dir"

    /**
     * Write thumbnail image to disk
     */
    const val WRITE_THUMBNAIL = "write-thumbnail"

    /**
     * Write all thumbnail image formats to disk
     */
    const val WRITE_ALL_THUMBNAILS = "write-all-thumbnails"

    /**
     * Simulate and list all available thumbnail
     * formats
     */
    const val LIST_THUMBNAILS = "list-thumbnails"

    /**
     * With arg: ENCODING
     * Force the specified encoding (experimental)
     */
    const val ENCODING = "encoding"

    /**
     * Suppress HTTPS certificate validation
     */
    const val NO_CHECK_CERTIFICATE = "no-check-certificate"

    /**
     * With arg: SECONDS
     * Number of seconds to sleep before each
     * download when used alone or a lower bound
     * of a range for randomized sleep before each
     * download (minimum possible number of
     * seconds to sleep) when used along with
     * --max-sleep-interval.
     */
    const val SLEEP_INTERVAL = "sleep-interval"

    /**
     * With arg: SECONDS
     * Upper bound of a range for randomized sleep
     * before each download (maximum possible
     * number of seconds to sleep). Must only be
     * used along with --min-sleep-interval.
     */
    const val MAX_SLEEP_INTERVAL = "max-sleep-interval"

    /**
     * With arg: FORMAT
     * Video format code, see the "FORMAT
     * SELECTION" for all the info
     */
    const val FORMAT = "format"

    /**
     * Download all available video formats
     */
    const val ALL_FORMATS = "all-formats"

    /**
     * Prefer free video formats unless a specific
     * one is requested
     */
    const val PREFER_FREE_FORMATS = "prefer-free-formats"

    /**
     * List all available formats of requested
     * videos
     */
    const val LIST_FORMATS = "list-formats"

    /**
     * Do not download the DASH manifests and
     * related data on YouTube videos
     */
    const val YOUTUBE_SKIP_DASH_MANIFEST = "youtube-skip-dash-manifest"

    /**
     * With arg: FORMAT
     * If a merge is required (e.g.
     * bestvideo+bestaudio), output to given
     * container format. One of mkv, mp4, ogg,
     * webm, flv. Ignored if no merge is required
     */
    const val MERGE_OUTPUT_FORMAT = "merge-output-format"

    /**
     * Write subtitle file
     */
    const val WRITE_SUB = "write-sub"

    /**
     * Write automatically generated subtitle file
     * (YouTube only)
     */
    const val WRITE_AUTO_SUB = "write-auto-sub"

    /**
     * Download all the available subtitles of the
     * video
     */
    const val ALL_SUBS = "all-subs"

    /**
     * List all available subtitles for the video
     */
    const val LIST_SUBS = "list-subs"

    /**
     * With arg: FORMAT
     * Subtitle format, accepts formats
     * preference, for example: "srt" or
     * "ass/srt/best"
     */
    const val SUB_FORMAT = "sub-format"

    /**
     * With arg: LANGS
     * Languages of the subtitles to download
     * (optional) separated by commas, use --list-
     * subs for available language tags
     */
    const val SUB_LANG = "sub-lang"

    /**
     * With arg: USERNAME
     * Login with this account ID
     */
    const val USERNAME = "username"

    /**
     * With arg: PASSWORD
     * Account password. If this option is left
     * out, youtube-dl will ask interactively.
     */
    const val PASSWORD = "password"

    /**
     * With arg: TWOFACTOR
     * Two-factor authentication code
     */
    const val TWOFACTOR = "twofactor"

    /**
     * Use .netrc authentication data
     */
    const val NETRC = "netrc"

    /**
     * With arg: PASSWORD
     * Video password (vimeo, smotri, youku)
     */
    const val VIDEO_PASSWORD = "video-password"

    /**
     * Convert video files to audio-only files
     * (requires ffmpeg or avconv and ffprobe or
     * avprobe)
     */
    const val EXTRACT_AUDIO = "extract-audio"

    /**
     * With arg: FORMAT
     * Specify audio format: "best", "aac",
     * "flac", "mp3", "m4a", "opus", "vorbis", or
     * "wav"; "best" by default; No effect without
     * -x
     */
    const val AUDIO_FORMAT = "audio-format"

    /**
     * With arg: QUALITY
     * Specify ffmpeg/avconv audio quality, insert
     * a value between 0 (better) and 9 (worse)
     * for VBR or a specific bitrate like 128K
     * (default 5)
     */
    const val AUDIO_QUALITY = "audio-quality"

    /**
     * With arg: FORMAT
     * Encode the video to another format if
     * necessary (currently supported:
     * mp4|flv|ogg|webm|mkv|avi)
     */
    const val RECODE_VIDEO = "recode-video"

    /**
     * With arg: ARGS
     * Give these arguments to the postprocessor
     */
    const val POSTPROCESSOR_ARGS = "postprocessor-args"

    /**
     * Keep the video file on disk after the post-
     * processing; the video is erased by default
     */
    const val KEEP_VIDEO = "keep-video"

    /**
     * Do not overwrite post-processed files; the
     * post-processed files are overwritten by
     * default
     */
    const val NO_POST_OVERWRITES = "no-post-overwrites"

    /**
     * Embed subtitles in the video (only for mp4,
     * webm and mkv videos)
     */
    const val EMBED_SUBS = "embed-subs"

    /**
     * Embed thumbnail in the audio as cover art
     */
    const val EMBED_THUMBNAIL = "embed-thumbnail"

    /**
     * Write metadata to the video file
     */
    const val ADD_METADATA = "add-metadata"

    /**
     * With arg: FORMAT
     * Parse additional metadata like song title /
     * artist from the video title. The format
     * syntax is the same as --output. Regular
     * expression with named capture groups may
     * also be used. The parsed parameters replace
     * existing values. Example: --metadata-from-
     * title "%(artist)s - %(title)s" matches a
     * title like "Coldplay - Paradise". Example
     * (regex): --metadata-from-title
     * "(?P<artist>.+?) - (?P<title>.+)"
     */
    const val METADATA_FROM_TITLE = "metadata-from-title"

    /**
     * Write metadata to the video file's xattrs
     * (using dublin core and xdg standards)
     */
    const val XATTRS = "xattrs"

    /**
     * With arg: POLICY
     * Automatically correct known faults of the
     * file. One of never (do nothing), warn (only
     * emit a warning), detect_or_warn (the
     * default; fix file if we can, warn
     * otherwise)
     */
    const val FIXUP = "fixup"

    /**
     * Prefer avconv over ffmpeg for running the
     * postprocessors
     */
    const val PREFER_AVCONV = "prefer-avconv"

    /**
     * Prefer ffmpeg over avconv for running the
     * postprocessors (default)
     */
    const val PREFER_FFMPEG = "prefer-ffmpeg"

    /**
     * With arg: PATH
     * Location of the ffmpeg/avconv binary;
     * either the path to the binary or its
     * containing directory.
     */
    const val FFMPEG_LOCATION = "ffmpeg-location"

    /**
     * With arg: CMD
     * Execute a command on the file after
     * downloading, similar to find's -exec
     * syntax. Example: --exec 'adb push {}
     * /sdcard/Music/ && rm {}'
     */
    const val EXEC = "exec"

    /**
     * With arg: FORMAT
     * Convert the subtitles to other format
     * (currently supported: srt|ass|vtt|lrc)
     */
    const val CONVERT_SUBS = "convert-subs"
}