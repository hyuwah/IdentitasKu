package com.muhammadwahyudin.identitasku.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.widget.doOnTextChanged
import com.easyfingerprint.EasyFingerPrint
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.muhammadwahyudin.identitasku.utils.lottieAnimationView
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


/**
 * A register & login screen that offers login via password/fingerprint.
 */
class LoginActivity : BaseActivity(), KodeinAware {
    override val kodein by closestKodein()
    val appDatabase by instance<AppDatabase>()

    private var isRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        isRegistered = Hawk.contains(Constants.SP_PASSWORD)

        if (BuildConfig.DEBUG)
            btn_login.setOnLongClickListener {
                alert(
                    Appcompat
                ) {
                    title = getString(R.string.dialog_title_register_success)
                    this.customView {
                        linearLayout {
                            this.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            this.gravity = Gravity.CENTER
                            this.orientation = LinearLayout.VERTICAL
                            lottieAnimationView {
                                this.layoutParams = ViewGroup.LayoutParams(dip(128), dip(128))
                                this.adjustViewBounds = true
                                this.setAnimation(R.raw.success)
                                this.loop(false)
                                this.playAnimation()
                            }
                            positiveButton("Continue") {
                                it.dismiss()
                            }
                        }

                    }
                    isCancelable = false
                    show()
                }
                true
            }

        if (isRegistered) { // Login
            btn_login.setOnClickListener {
                validateLogin()
            }
        } else { // First open / register
            tv_title.text = getString(R.string.register_title)
            btn_login.text = getString(R.string.button_register)
            textView2.visibility = View.GONE
            til_password_confirm.visibility = View.VISIBLE
            til_password_confirm.isEnabled = false
            btn_login.isEnabled = false
            btn_login_fp.hide()
            btn_login.setOnClickListener {
                register()
            }
        }

        password.doOnTextChanged { text, _, _, _ ->
            til_password.isErrorEnabled = false
            if (!isRegistered) {
                til_password_confirm.isEnabled = !text.isNullOrEmpty()
            }
        }
        password_confirm.doOnTextChanged { text, _, _, _ ->
            til_password_confirm.isErrorEnabled = false
            if (!isRegistered) {
                btn_login.isEnabled = !text.isNullOrEmpty()
            }
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
            isRegistered
        ) {
            btn_login_fp.performClick()
        }
    }

    private fun loginWithFingerprint() {
        EasyFingerPrint(this)
            .setTittle(getString(R.string.login_fingerprint_title))
            .setSubTittle(getString(R.string.app_name))
            .setColorPrimary(R.color.grey_300)
            .setDescription(getString(R.string.login_fingerprint_desc))
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
            til_password.error = getString(R.string.text_hint_login_password_invalid)
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
                    alert(
                        Appcompat
                    ) {
                        title = getString(R.string.dialog_title_register_success)
                        this.customView {
                            linearLayout {
                                this.layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                this.gravity = Gravity.CENTER
                                this.orientation = LinearLayout.VERTICAL
                                lottieAnimationView {
                                    this.layoutParams = ViewGroup.LayoutParams(dip(128), dip(128))
                                    this.adjustViewBounds = true
                                    this.setAnimation(R.raw.success)
                                    this.loop(false)
                                    this.playAnimation()
                                }
                                positiveButton("Continue") {
                                    it.dismiss()
                                }
                            }

                        }
                        isCancelable = false
                        show()
                    }
                } else {
                    til_password_confirm.error = getString(R.string.text_hint_register_password_confirmation_not_match)
                    til_password_confirm.isErrorEnabled = true
                }
            }
            passwordEdt.isBlank() -> {
                til_password.error = getString(R.string.text_hint_register_password_empty)
                til_password.isErrorEnabled = true
            }
            passwordEdt.isNotBlank() && passwordConfirmEdt.isBlank() -> {
                til_password_confirm.error = getString(R.string.text_hint_register_password_confirmation_empty)
                til_password_confirm.isErrorEnabled = true
            }
        }
    }
}
