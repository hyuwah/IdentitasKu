package com.muhammadwahyudin.identitasku.ui.login

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.orhanobut.hawk.Hawk
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

        if (Hawk.contains("password")) {
            til_password.editText?.text = Hawk.get("password")
        }

        til_password.editText?.doOnTextChanged { text, start, count, after ->
            til_password.isErrorEnabled = false
        }

        btn_login.setOnClickListener {
            validateLogin()
        }
    }

    fun validateLogin() {
        if (til_password.editText!!.text.isNotBlank()) {
            Hawk.put("password", til_password.editText!!.text.toString())
            startActivity(intentFor<HomeActivity>().clearTop())
            finish()
        } else {
            til_password.error = "Password can't be empty"
            til_password.isErrorEnabled = true
        }
    }
}
