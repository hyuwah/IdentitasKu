package com.muhammadwahyudin.identitasku.ui._helper

import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar

abstract class SafeClickListener : Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private var lastClickInstant: Long = 0

    open fun onClick() {}

    open fun onClick(item: MenuItem) {}

    override fun onMenuItemClick(item: MenuItem): Boolean {
        throttleClick(item)
        return true
    }

    override fun onClick(v: View?) {
        throttleClick(null)
    }

    private fun throttleClick(item: MenuItem?) {
        if (SystemClock.elapsedRealtime() - lastClickInstant < 1000) {
            return
        }
        lastClickInstant = SystemClock.elapsedRealtime()
        if (item == null)
            onClick()
        else
            onClick(item)
    }
}