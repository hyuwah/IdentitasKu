package com.muhammadwahyudin.identitasku.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.widget.doOnTextChanged
import com.commonsware.cwac.saferoom.SQLCipherUtils
import com.easyfingerprint.EasyFingerPrint
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(), KodeinAware {
    override val kodein by closestKodein()
    val appDatabase by instance<AppDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // toast("DB ${appDatabase.openHelper.readableDatabase.path}")

        if (Hawk.contains("password")) { // Login
            btn_login.setOnClickListener {
                validateLogin()
            }
        } else { // First open / register
            tv_title.text = "REGISTER"
            btn_login.text = "Register"
            textView2.visibility = View.GONE
            til_password_confirm.visibility = View.VISIBLE
            btn_login_fp.hide()
            btn_login.setOnClickListener {
                register()
            }
        }

        til_password.editText?.doOnTextChanged { _, _, _, _ ->
            til_password.isErrorEnabled = false
        }
        til_password_confirm.editText?.doOnTextChanged { _, _, _, _ ->
            til_password_confirm.isErrorEnabled = false
        }

        btn_login_fp.setOnClickListener {
            loginWithFingerprint()
        }

        // Hide login with fingerprint if device has no sensor
        if (!FingerprintManagerCompat.from(this).isHardwareDetected) {
            textView2.visibility = View.GONE
            btn_login_fp.hide()
        }
    }

    private fun loginWithFingerprint() {
        EasyFingerPrint(this)
            .setTittle("Login with fingerprint")
            .setSubTittle("IdentitasKU")
            .setColorPrimary(R.color.grey_300)
            .setDescription("Use your Fingerprint to login.")
            .setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher_round))
            .setListern(object : EasyFingerPrint.ResultFingerPrintListern {
                override fun onError(mensage: String, code: Int) {
                    when (code) {
                        EasyFingerPrint.CODE_ERRO_CANCEL -> {
                        } // TO DO
                        EasyFingerPrint.CODE_ERRO_GREATER_ANDROID_M -> {
                        } // TO DO
                        EasyFingerPrint.CODE_ERRO_HARDWARE_NOT_SUPPORTED -> {
                        } // TO DO
                        EasyFingerPrint.CODE_ERRO_NOT_ABLED -> {
                        } // TO DO
                        EasyFingerPrint.CODE_ERRO_NOT_FINGERS -> {
                        } // TO DO
                        EasyFingerPrint.CODE_NOT_PERMISSION_BIOMETRIC -> {
                        } // TO DO
                    }
                    toast("Error: $mensage / $code")
                }

                override fun onSucess(cryptoObject: FingerprintManagerCompat.CryptoObject?) {
                    startActivity(intentFor<HomeActivity>().clearTop())
                    finish()
                }
            })
            .startScan()
    }

    private fun validateLogin() {
        val passwordEdt = til_password.editText!!.text
        if (passwordEdt.isNotBlank() && passwordEdt.toString() == Hawk.get("password")) {
            startActivity(intentFor<HomeActivity>().clearTop())
            finish()
        } else {
            val dbState = SQLCipherUtils.getDatabaseState(this, Constants.DB_NAME)
            toast("DB State: ${dbState.name}")
            til_password.error = "Wrong password"
            til_password.isErrorEnabled = true
        }
    }

    private fun register() {
        val passwordEdt = til_password.editText!!.text
        val passwordConfirmEdt = til_password_confirm.editText!!.text
        when {
            passwordEdt.isNotBlank() && passwordConfirmEdt.isNotBlank() -> {
                if (passwordEdt.toString() == passwordConfirmEdt.toString()) {
                    Hawk.put("password", passwordEdt.toString())
                    alert {
                        iconResource = R.drawable.identity_black_256
                        isCancelable = false
                        title = "Register Success"
                        message = "Click continue to enter"
                        positiveButton("Continue") {
                            startActivity(intentFor<HomeActivity>().clearTop())
                            finish()
                        }
                        show()
                    }
                } else {
                    til_password_confirm.error = "Password does not match"
                    til_password_confirm.isErrorEnabled = true
                }
            }
            passwordEdt.isBlank() -> {
                til_password.error = "Password can't be empty"
                til_password.isErrorEnabled = true
            }
            passwordEdt.isNotBlank() && passwordConfirmEdt.isBlank() -> {
                til_password_confirm.error = "Please re-enter your password"
                til_password_confirm.isErrorEnabled = true
            }
        }
    }
}
