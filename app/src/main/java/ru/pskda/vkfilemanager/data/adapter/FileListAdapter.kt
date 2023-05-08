package ru.pskda.vkfilemanager.data.adapter

import android.content.Context
import android.text.format.Formatter.formatFileSize
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.pskda.vkfilemanager.R
import ru.pskda.vkfilemanager.data.formatter.FileTypeFormatter.getFileType
import ru.pskda.vkfilemanager.data.formatter.FileTypeFormatter.getFileTypeIcon
import ru.pskda.vkfilemanager.ui.menu.FileListPopup
import ru.pskda.vkfilemanager.utils.intent.IntentManager.openDirectory
import ru.pskda.vkfilemanager.utils.intent.IntentManager.openFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FileListAdapter(var context: Context, var files: Array<File>) :
    RecyclerView.Adapter<FileListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.fileNameTV)
        var date: TextView = itemView.findViewById(R.id.fileDateTV)
        var size: TextView = itemView.findViewById(R.id.fileSizeTV)

        var icon: ImageView = itemView.findViewById(R.id.fileIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.file_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currFile = files[position]

        with(holder) {
            name.text = currFile.name

            if (!currFile.isDirectory)
                size.text = formatFileSize(context, currFile.length())

            date.text = SimpleDateFormat(
                "dd/MM/yy hh:mm a",
                Locale.getDefault()
            ).format(Date(currFile.lastModified()))

            icon.setImageResource(getFileTypeIcon(getFileType(currFile)))

            itemView.setOnClickListener {
                if (currFile.isDirectory) {
                    openDirectory(context, currFile)
                } else {
                    openFile(context, currFile)
                }
            }

            itemView.setOnLongClickListener {
                val popup = FileListPopup(context, it, currFile)
                popup.show()

                true
            }
        }
    }
}