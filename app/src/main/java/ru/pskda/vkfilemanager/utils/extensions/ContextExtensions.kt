package ru.pskda.vkfilemanager.utils.extensions

import android.view.View

object ContextExtensions {
    fun View.visible(b: Boolean){
        if (b) this.visibility = View.VISIBLE
        else this.visibility = View.GONE
    }
}