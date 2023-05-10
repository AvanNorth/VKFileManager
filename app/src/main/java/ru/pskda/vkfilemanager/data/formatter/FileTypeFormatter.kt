package ru.pskda.vkfilemanager.data.formatter

import android.webkit.MimeTypeMap
import ru.pskda.vkfilemanager.R
import java.io.File

object FileTypeFormatter {

    enum class FileType {
        DIRECTORY, TEXT, AUDIO, VIDEO, APK, OTHER, IMAGE, DOCUMENT, PDF, GIF, ARCHIVE
    }

    fun getFileTypeIcon(type: FileType): Int {
        return when (type) {
            FileType.DIRECTORY -> R.drawable.baseline_folder_24
            FileType.IMAGE -> R.drawable.baseline_image_24
            FileType.AUDIO -> R.drawable.baseline_music_24
            FileType.VIDEO -> R.drawable.baseline_video_file_24
            FileType.DOCUMENT -> R.drawable.baseline_article_24
            FileType.TEXT -> R.drawable.baseline_edit_note_24
            FileType.ARCHIVE -> R.drawable.baseline_archive_24
            FileType.PDF -> R.drawable.baseline_pdf_24
            FileType.GIF -> R.drawable.baseline_gif_24
            FileType.APK -> R.drawable.baseline_android_24
            FileType.OTHER -> R.drawable.baseline_file_24
        }
    }

    /**
     * Функция возвращает тип файла, согласно классификации по FileTypeFormatter.
     */
    fun getFileType(file: File): FileType {
        return if (file.isDirectory) FileType.DIRECTORY
        else {
            when (getFileExt(file)) {
                "apk" -> FileType.APK
                "pdf" -> FileType.PDF
                "gif" -> FileType.GIF
                "jar", "7zip", "rar" -> FileType.ARCHIVE
                "doc", "docx", "wpd", "odt" -> FileType.DOCUMENT
                "png", "svg", "jpeg", "webp", "jpg" -> FileType.IMAGE
                "mp3", "m4a", "flac", "wav", "aac" -> FileType.AUDIO
                "txt", "css", "html", "rtf", "php", "xml", "csv", "cmd" -> FileType.TEXT
                "flv", "mp4", "mpg", "mpeg", "webm", "3gp", "mov", "mkv", "avi" -> FileType.VIDEO
                else -> FileType.OTHER
            }
        }
    }

    /**
     * Функция возвращает mime-тип файла согласно базе типов класса MimeTypeMap.
     */
    fun getFileMIMEType(file: File): String? = MimeTypeMap.getSingleton()
        .getMimeTypeFromExtension(getFileExt(file).lowercase())

    fun getFileExt(file: File): String = file.extension
}