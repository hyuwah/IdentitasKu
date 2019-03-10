package com.muhammadwahyudin.identitasku.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.provider.Settings
import org.jetbrains.anko.toast

object Commons {
    fun copyToClipboard(ctx: Context, value: String, type: String) {
        val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(ctx.packageName, value)
        clipboard.primaryClip = clip
        ctx.toast("$type copied to clipboard")
    }

    fun getDeviceId(ctx: Context): String {
        return Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
    }
}