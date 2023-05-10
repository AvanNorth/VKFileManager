package ru.pskda.vkfilemanager.utils.sort

import ru.pskda.vkfilemanager.data.formatter.FileTypeFormatter
import java.io.File

object FileSorting {
    fun sortFilesByExt(list: List<File>) =
        list.sortedWith { lf, rf ->
            val lfExt = FileTypeFormatter.getFileExt(lf)
            val rfExt = FileTypeFormatter.getFileExt(rf)

            if (lfExt > rfExt) 1 else if (lfExt < rfExt) -1 else 0
        }

    fun sortFilesByExtInv(list: List<File>) =
        list.sortedWith { lf, rf ->
            val lfExt = FileTypeFormatter.getFileExt(lf)
            val rfExt = FileTypeFormatter.getFileExt(rf)

            if (lfExt > rfExt) -1 else if (lfExt < rfExt) 1 else 0
        }

    fun sortFilesBySize(list: List<File>) =
        list.sortedWith { lf, rf ->
            lf.length().compareTo(rf.length())
        }

    fun sortFilesBySizeInv(list: List<File>) =
        list.sortedWith { lf, rf ->
            -1 * lf.length().compareTo(rf.length())
        }

    fun sortFilesByDate(list: List<File>) =
        list.sortedWith { lf, rf ->
            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
            lf.lastModified().compareTo(rf.lastModified())
        }

    fun sortFilesByDateInv(list: List<File>) =
        list.sortedWith { lf, rf ->
            -1 * lf.lastModified().compareTo(rf.lastModified())
        }

    fun sortFilesByName(list: List<File>): List<File> =
        list.sortedWith { lf, rf ->
            if (lf.name > rf.name) 1 else if (lf.name < rf.name) -1 else 0
        }

    fun sortFilesByNameInv(list: List<File>) =
        list.sortedWith { lf, rf ->
            if (lf.name > rf.name) -1 else if (lf.name < rf.name) 1 else 0
        }
}