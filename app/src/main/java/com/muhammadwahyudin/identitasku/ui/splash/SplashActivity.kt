package com.muhammadwahyudin.identitasku.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.ui.login.LoginActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(intentFor<LoginActivity>().clearTask())
            finish()
        }, 2500)
    }
}
