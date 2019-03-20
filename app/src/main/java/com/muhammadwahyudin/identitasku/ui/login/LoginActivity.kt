package com.muhammadwahyudin.identitasku.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.widget.doOnTextChanged
import com.easyfingerprint.EasyFingerPrint
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


/**
 * A register & login screen that offers login via password/fingerprint.
 */
class LoginActivity : BaseActivity(), KodeinAware {
    override val kodein by closestKodein()
    val appDatabase by instance<AppDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // toast("DB ${appDatabase.openHelper.readableDatabase.path}")

        if (Hawk.contains(Constants.SP_PASSWORD)) { // Login
            btn_login.setOnClickListener {
                validateLogin()
            }
        } else { // First open / register
            tv_title.text = getString(R.string.register_title)
            btn_login.text = getString(R.string.register_title)
            textView2.visibility = View.GONE
            til_password_confirm.visibility = View.VISIBLE
            btn_login_fp.hide()
            btn_login.setOnClickListener {
                register()
            }
        }

        password.doOnTextChanged { _, _, _, _ ->
            til_password.isErrorEnabled = false
        }
        password_confirm.doOnTextChanged { _, _, _, _ ->
            til_password_confirm.isErrorEnabled = false
        }

        btn_login_fp.setOnClickListener {
            loginWithFingerprint()
        }

        // Hide login with fingerprint if device has no sensor
        if (!FingerprintManagerCompat.from(this).isHardwareDetected) {
            textView2.visibility = View.GONE
            btn_login_fp.hide()
        } // Show fingerprint login, if has sensor, has enrolled & has registered
        else if (FingerprintManagerCompat.from(this).isHardwareDetected &&
            FingerprintManagerCompat.from(this).hasEnrolledFingerprints() &&
            Hawk.contains(Constants.SP_PASSWORD)
        ) {
            btn_login_fp.performClick()
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
                            toast("ANDROID M")
                        } // TO DO
                        EasyFingerPrint.CODE_ERRO_HARDWARE_NOT_SUPPORTED -> {
                        } // TO DO
                        EasyFingerPrint.CODE_ERRO_NOT_ABLED -> {
                            alert(Appcompat, "You need to secure your device with fingerprint first", "Oops..") {
                                positiveButton("Add fingerprint") {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                                        startActivity(Intent(Settings.ACTION_FINGERPRINT_ENROLL))
                                    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                        startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
                                }
                                show()
                            }
                        } // TO DO
                        EasyFingerPrint.CODE_ERRO_NOT_FINGERS -> {
                            toast("NOT FINGER")
                        } // TO DO
                        EasyFingerPrint.CODE_NOT_PERMISSION_BIOMETRIC -> {
                            toast("BIOMETRIC")
                        } // TO DO
                    }
                }

                override fun onSucess(cryptoObject: FingerprintManagerCompat.CryptoObject?) {
                    startActivity(intentFor<HomeActivity>().clearTop())
                    finish()
                }
            })
            .startScan()
    }

    private fun validateLogin() {
        val passwordEdt = password.text!!
        if (passwordEdt.isNotBlank() && passwordEdt.toString() == Hawk.get(Constants.SP_PASSWORD)) {
            startActivity(intentFor<HomeActivity>().clearTop())
            finish()
        } else {
            til_password.error = "Wrong password"
            til_password.isErrorEnabled = true
        }
    }

    private fun register() {
        val passwordEdt = password.text!!
        val passwordConfirmEdt = password_confirm.text!!
        when {
            passwordEdt.isNotBlank() && passwordConfirmEdt.isNotBlank() -> {
                if (passwordEdt.toString() == passwordConfirmEdt.toString()) {
                    Hawk.put(Constants.SP_PASSWORD, passwordEdt.toString())
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
