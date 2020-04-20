package com.muhammadwahyudin.identitasku.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.inputmethod.InputMethodManager
import java.util.*

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
        clipboard.setPrimaryClip(clip)
        (ctx as Activity).toast("$type copied to clipboard")
    }

    /**
     * Method to get UUID. Used as unique password for saferoom
     * @return String of device id
     */
    fun getUUID(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * Method to dismiss soft keyboard
     * @param activity
     */
    fun hideSoftKeyboard(activity: Activity) {
        // Check if no view has focus:
        val view = activity.currentFocus
        view?.let { v ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    /**
     *  Method to vibrate phone (150 ms)
     *  @param context
     */
    fun shortVibrate(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            (context.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(
                VibrationEffect.createOneShot(
                    150,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            (context.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(150)
        }
    }
}