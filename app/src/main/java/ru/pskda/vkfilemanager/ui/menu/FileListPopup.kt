package ru.pskda.vkfilemanager.ui.menu

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import ru.pskda.vkfilemanager.R
import ru.pskda.vkfilemanager.utils.extensions.ContextExtensions.visible
import ru.pskda.vkfilemanager.utils.intent.IntentManager
import java.io.File

class FileListPopup(context: Context, v: View, file: File) {
    private val popup = PopupMenu(context, v)

    init {
        popup.menu.add(context.getString(R.string.rename_popup_text))
        popup.menu.add(context.getString(R.string.delete_popup_text))
        popup.menu.add(context.getString(R.string.move_popup_text))
        popup.menu.add(context.getString(R.string.share_popup_text))

        popup.setOnMenuItemClickListener {
            when (it.title) {
                context.getString(R.string.delete_popup_text) -> {
                    val b = file.delete()

                    if (b)
                        Toast.makeText(
                            context.applicationContext,
                            file.name + " удален",
                            Toast.LENGTH_SHORT
                        ).show()

                    v.visible(!b)
                }

                context.getString(R.string.share_popup_text) -> {
                    IntentManager.shareFile(context, file)
                }

                //TODO Добавить остальные пункты меню
            }
            true
        }
    }

    fun show(){
        popup.show()
    }
}