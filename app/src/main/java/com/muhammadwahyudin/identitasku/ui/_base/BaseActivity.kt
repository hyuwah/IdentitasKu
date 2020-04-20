package com.muhammadwahyudin.identitasku.ui._base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.utils.toast

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private var backToExitPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Disable Screenshot
        if (!BuildConfig.DEBUG) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }

    // Double back to exit
    override fun onBackPressed() {
        if (backToExitPressed || supportFragmentManager.backStackEntryCount != 0) {
            super.onBackPressed()
            return
        }
        this.backToExitPressed = true
        toast(getString(R.string.exit_double_tap_message))
        Handler().postDelayed({ backToExitPressed = false }, 2000)
    }
}