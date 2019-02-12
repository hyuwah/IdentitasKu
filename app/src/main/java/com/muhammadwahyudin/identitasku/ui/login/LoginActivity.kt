package com.muhammadwahyudin.identitasku.ui.login

import android.os.Bundle
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email_sign_in_button.setOnClickListener {
            startActivity(intentFor<HomeActivity>().clearTop())
            finish()
        }
    }
}
