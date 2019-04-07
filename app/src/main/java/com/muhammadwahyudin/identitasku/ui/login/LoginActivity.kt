package com.muhammadwahyudin.identitasku.ui.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricConstants
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.widget.doOnTextChanged
import com.github.pwittchen.rxbiometric.library.RxBiometric
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationError
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationFail
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationHelp
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui._views.RegisterSuccessDialog
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.orhanobut.hawk.Hawk
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
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
    private var wrongPasswordInputAttempt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        isRegistered = Hawk.contains(Constants.SP_PASSWORD)

        if (BuildConfig.DEBUG)
            btn_login.setOnLongClickListener {
                val registerSuccessDialog = RegisterSuccessDialog(AnkoContext.create(this, contentView!!))
                registerSuccessDialog.onPositiveBtnClick = {
                    registerSuccessDialog.dialog.dismiss()
                }
                true
            }

        if (isRegistered) { // Login
            btn_login.setOnClickListener {
                if (wrongPasswordInputAttempt > 3)
                    showForgotPasswordDialog()
                else
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

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    var disposable: Disposable? = null
    private fun loginWithFingerprint() {
        disposable?.dispose()
        disposable = RxBiometric
            .title(getString(R.string.login_fingerprint_title))
            .description(getString(R.string.login_fingerprint_desc))
            .negativeButtonText(getString(R.string.dialog_btn_cancel))
            .negativeButtonListener(DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .executor(mainExecutor)
            .build()
            .authenticate(this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    startActivity(intentFor<HomeActivity>().clearTop())
                    finish()
                },
                onError = {
                    when (it) {
                        is AuthenticationError -> {
//                            toast("${it.errorCode} : ${it.errorMessage}")
                            when (it.errorCode) {
                                BiometricConstants.ERROR_NO_BIOMETRICS -> {
                                    showNeedToAddFingerprintDialog()
                                }
                                BiometricConstants.ERROR_LOCKOUT -> {
                                    toast("Too many attempts. Please try again later...")
                                }
                                BiometricConstants.ERROR_UNABLE_TO_PROCESS -> {
                                }
                                BiometricConstants.ERROR_TIMEOUT -> {
                                }
                            }
                        }
                        is AuthenticationFail -> {
//                            toast("${it.message}")
                        }
                        is AuthenticationHelp -> {
//                            toast("auth help")
                        }
//                        else -> toast("error ${it.message}")
                    }
                }
            )
    }

    private fun validateLogin() {
        val passwordEdt = password.text!!
        if (passwordEdt.isNotBlank() && passwordEdt.toString() == Hawk.get(Constants.SP_PASSWORD)) {
            startActivity(intentFor<HomeActivity>().clearTop())
            finish()
        } else {
            til_password.error = getString(R.string.text_hint_login_password_invalid)
            til_password.isErrorEnabled = true
            wrongPasswordInputAttempt += 1
        }
    }

    private fun register() {
        val passwordEdt = password.text!!
        val passwordConfirmEdt = password_confirm.text!!
        when {
            passwordEdt.isNotBlank() && passwordConfirmEdt.isNotBlank() -> {
                if (passwordEdt.toString() == passwordConfirmEdt.toString()) {
                    Hawk.put(Constants.SP_PASSWORD, passwordEdt.toString())
                    val registerSuccessDialog = RegisterSuccessDialog(AnkoContext.create(this, contentView!!))
                    registerSuccessDialog.onPositiveBtnClick = {
                        startActivity(intentFor<HomeActivity>().clearTop())
                        registerSuccessDialog.dialog.dismiss()
                        finish()
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

    private fun resetPassword() {
        alert(Appcompat) {
            title = getString(R.string.dialog_title_forgot_password_confirmation)
            message = getString(R.string.dialog_message_forgot_password_confirmation)
            positiveButton(getString(R.string.dialog_btn_no)) { it.dismiss() }
            negativeButton(getString(R.string.dialog_btn_yes)) {
                Hawk.deleteAll()
                doAsync {
                    appDatabase.dataDao().deleteAll()
                }
                it.dismiss()
                recreate()
            }
        }.show().apply {
            getButton(AlertDialog.BUTTON_NEGATIVE).textColor = ContextCompat.getColor(ctx, R.color.red_500)
        }
    }

    // TODO extract string
    private fun showForgotPasswordDialog() {
        alert(Appcompat) {
            title = getString(R.string.dialog_title_forgot_password)
            message = getString(R.string.dialog_message_forgot_password)
            isCancelable = false
            negativeButton(getString(R.string.dialog_btn_action_forgot_password)) {
                resetPassword()
                it.dismiss()
            }
            positiveButton(getString(R.string.dialog_btn_cancel_forgot_password)) {
                it.dismiss()
            }
        }.show().apply {
            getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                textColor = ContextCompat.getColor(ctx, R.color.red_500)
            }
        }
    }

    // TODO extract string
    private fun showNeedToAddFingerprintDialog() {
        alert(
            Appcompat,
            getString(R.string.dialog_message_fingerprint_not_enrolled),
            getString(R.string.dialog_title_fingerprint_not_enrolled)
        ) {
            positiveButton(getString(R.string.dialog_btn_action_fingerprint_not_enrolled)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    startActivity(Intent(Settings.ACTION_FINGERPRINT_ENROLL))
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
            }
            show()
        }
    }
}
