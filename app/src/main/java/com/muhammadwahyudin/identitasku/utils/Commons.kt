package com.muhammadwahyudin.identitasku.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.provider.Settings
import org.jetbrains.anko.toast

/**
 * Singleton class of common functions that used within the project
 */
object Commons {
    /**
     * Method to copy [value] of to the clipboard
     * @param ctx Context
     * @param value String to copy
     * @param type String to show on toast
     */
    fun copyToClipboard(ctx: Context, value: String, type: String) {
        val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(ctx.packageName, value)
        clipboard.primaryClip = clip
        ctx.toast("$type copied to clipboard")
    }

    /**
     * Method to get device Id. Used as unique password for saferoom
     * @param ctx Context
     * @return String of device id
     */
    fun getDeviceId(ctx: Context): String {
        return Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
    }
}