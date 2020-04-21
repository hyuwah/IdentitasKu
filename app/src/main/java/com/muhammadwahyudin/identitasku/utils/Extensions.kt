package com.muhammadwahyudin.identitasku.utils

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.widget.PopupMenu

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

@SuppressLint("RestrictedApi")
fun PopupMenu.showIcons() {
    try {
        val fMenuHelper = PopupMenu::class.java.getDeclaredField("mPopup")
        fMenuHelper.isAccessible = true
        val menuHelper = fMenuHelper.get(this)
        val argTypes = Boolean::class.javaPrimitiveType
        menuHelper.javaClass
            .getDeclaredMethod("setForceShowIcon", argTypes)
            .invoke(menuHelper, true)
    } catch (e: Exception) {
    }
}