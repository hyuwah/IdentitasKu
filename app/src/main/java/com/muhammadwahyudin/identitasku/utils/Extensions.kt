package com.muhammadwahyudin.identitasku.utils

import android.annotation.SuppressLint
import android.os.Build
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.bottomappbar.BottomAppBar
import com.muhammadwahyudin.identitasku.ui._helper.SafeClickListener

fun View.setVisibleIf(predicate: Boolean, invisible: Boolean = false) {
    when {
        predicate -> setVisible()
        invisible -> setInvisible()
        else -> setGone()
    }
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.setGone() {
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
        println(e.message)
    }
}

inline fun View.setSafeOnClickListener(crossinline callback: () -> Unit) =
    setOnClickListener(object : SafeClickListener() {
        override fun onClick() {
            callback()
        }
    })

inline fun BottomAppBar.setSafeOnMenuItemClickListener(crossinline callback: (item: MenuItem) -> Unit) =
    setOnMenuItemClickListener(object : SafeClickListener() {
        override fun onClick(item: MenuItem) {
            callback(item)
        }
    })

fun View.adjustInsets(
    statusBar: Boolean = false,
    navigationBar: Boolean = false,
    onApply: (View, Insets) -> Unit = { view, insets ->
        view.updatePadding(top = insets.top, bottom = insets.bottom)
    }
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
            val typeMask = when {
                statusBar && navigationBar -> WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
                statusBar -> WindowInsetsCompat.Type.statusBars()
                navigationBar -> WindowInsetsCompat.Type.navigationBars()
                else -> 0
            }
            val insets = windowInsets.getInsets(typeMask)
            onApply(view, insets)
            WindowInsetsCompat.CONSUMED
        }
    }
}