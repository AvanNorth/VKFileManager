package ru.pskda.vkfilemanager.utils.intent

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import ru.pskda.vkfilemanager.data.formatter.FileTypeFormatter
import ru.pskda.vkfilemanager.ui.activity.FileListActivity
import java.io.File

object IntentManager {
    fun shareFile(context: Context, file: File) {
        val intent = Intent()
        val fileMIMEType = FileTypeFormatter.getFileMIMEType(file) ?: "*/*"

        intent.setDataAndType(file.absolutePath.toUri(), fileMIMEType)
        intent.action = Intent.ACTION_SEND
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(intent)
    }

    fun openFile(context: Context, file: File) {
        val intent = Intent()
        val fileMIMEType = FileTypeFormatter.getFileMIMEType(file) ?: "*/*"

        intent.setDataAndType(file.absolutePath.toUri(), fileMIMEType)
        intent.action = Intent.ACTION_VIEW
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        if (intent.resolveActivity(context.packageManager) == null)
            intent.setDataAndType(file.absolutePath.toUri(), "*/*")

        context.startActivity(intent)
    }

    fun openDirectory(context: Context, file: File) {
        val intent = Intent(context, FileListActivity::class.java)

        intent.putExtra("storagePath", file.absolutePath)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(intent)
    }
}