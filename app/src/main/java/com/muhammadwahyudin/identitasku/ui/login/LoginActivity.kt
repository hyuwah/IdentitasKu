package com.muhammadwahyudin.identitasku.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.jakewharton.processphoenix.ProcessPhoenix
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.biometric.BiometricCallback
import com.muhammadwahyudin.identitasku.biometric.BiometricManager
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui._helper.TutorialHelper
import com.muhammadwahyudin.identitasku.ui._views.RegisterSuccessDialogs
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.muhammadwahyudin.identitasku.utils.BiometricUtils
import com.muhammadwahyudin.identitasku.utils.Commons
import com.muhammadwahyudin.identitasku.utils.toast
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

/**
 * A register & login screen that offers login via password/fingerprint.
 */
class LoginActivity : BaseActivity(), KodeinAware, CoroutineScope {
    override val kodein by closestKodein()
    override val coroutineContext: CoroutineContext
        get() = Job()
    private val appDatabase by instance<AppDatabase>()

    private var isRegistered = false
    private var wrongPasswordInputAttempt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        isRegistered = Hawk.contains(Constants.SP_PASSWORD)

        if (BuildConfig.DEBUG)
            btn_login.setOnLongClickListener {
                val dialog = RegisterSuccessDialogs()
                dialog.show(supportFragmentManager) {
                    dialog.dismiss()
                }
                true
            }

        if (isRegistered) { // Login
            btn_login.isEnabled = false
            btn_login.setOnClickListener {
                if (wrongPasswordInputAttempt > 3) {
                    Commons.hideSoftKeyboard(this)
                    showForgotPasswordDialog()
                    wrongPasswordInputAttempt = 0
                } else
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
            } else {
                btn_login.isEnabled = !text.isNullOrEmpty()
            }
        }
        password_confirm.doOnTextChanged { text, _, _, _ ->
            til_password_confirm.isErrorEnabled = false
            if (!isRegistered) {
                btn_login.isEnabled = !text.isNullOrEmpty()
            }
        }

        btn_login_fp.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (BiometricUtils.isFingerprintAvailable(this))
                    openFingerprintDialog()
                else
                    showNeedToAddFingerprintDialog()
            }
        }

        // Hide login with fingerprint if device has no sensor
        if (!BiometricUtils.isHardwareSupported(this)) {
            textView2.visibility = View.GONE
            btn_login_fp.hide()
        } // Show fingerprint login, if has sensor, has enrolled & has registered
        else if (
            BiometricUtils.isHardwareSupported(this) &&
            BiometricUtils.isFingerprintAvailable(this) &&
            isRegistered
        ) {
            btn_login_fp.performClick()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openFingerprintDialog() {
        BiometricManager.BiometricBuilder(this)
            .setTitle(getString(R.string.login_fingerprint_title))
            .setSubtitle(getString(R.string.app_name))
            .setDescription(getString(R.string.login_fingerprint_desc))
            .setNegativeButtonText(getString(R.string.dialog_btn_cancel))
            .build()
            .authenticate(object : BiometricCallback {
                override fun onBiometricAuthenticationNotAvailable() {
                }

                override fun onBiometricAuthenticationPermissionNotGranted() {
                    toast("Biometric auth permission not granted")
                }

                override fun onBiometricAuthenticationInternalError(error: String) {
                    toast("Biometric auth internal error: $error")
                }

                override fun onAuthenticationFailed() {
//                    toast("Auth failed")
                }

                override fun onAuthenticationCancelled() {
                }

                override fun onAuthenticationSuccessful() {
                    goToHomeActivity()
                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
//                    toast("Auth help ($helpCode) $helpString")
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                    toast("Auth error ($errorCode) $errString")
                }
            })
    }

    private fun goToHomeActivity() {
        SafeHelperFactory.rekey(
            appDatabase.openHelper.writableDatabase,
            Hawk.get<String>(Constants.SP_PASSWORD).toCharArray()
        )
        startActivity(Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
        finish()
    }

    private fun validateLogin() {
        val passwordEdt = password.text!!
        if (passwordEdt.isNotBlank() && passwordEdt.toString() == Hawk.get(Constants.SP_PASSWORD)) {
            goToHomeActivity()
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
                    // Reencrypt using user pass
                    SafeHelperFactory.rekey(appDatabase.openHelper.writableDatabase, passwordEdt)

                    val dialog = RegisterSuccessDialogs()
                    dialog.show(supportFragmentManager) {
                        startActivity(Intent(this, HomeActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        })
                        dialog.dismiss()
                        finish()
                    }
                } else {
                    til_password_confirm.error =
                        getString(R.string.text_hint_register_password_confirmation_not_match)
                    til_password_confirm.isErrorEnabled = true
                }
            }
            passwordEdt.isBlank() -> {
                til_password.error = getString(R.string.text_hint_register_password_empty)
                til_password.isErrorEnabled = true
            }
            passwordEdt.isNotBlank() && passwordConfirmEdt.isBlank() -> {
                til_password_confirm.error =
                    getString(R.string.text_hint_register_password_confirmation_empty)
                til_password_confirm.isErrorEnabled = true
            }
        }
    }

    private fun resetPassword() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_forgot_password_confirmation))
            .setMessage(getString(R.string.dialog_message_forgot_password_confirmation))
            .setPositiveButton(getString(R.string.dialog_btn_no)) { it, _ ->
                it.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_btn_yes)) { it, _ ->
                Hawk.deleteAll()
                TutorialHelper.resetTutorial(this)
                launch(Dispatchers.IO) {
                    appDatabase.dataDao().deleteAll()
                }
                deleteDatabase(appDatabase.openHelper.databaseName)
                it.dismiss()
                ProcessPhoenix.triggerRebirth(applicationContext)
            }
            .create()
        dialog.show()
    }

    private fun showForgotPasswordDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_forgot_password))
            .setMessage(getString(R.string.dialog_message_forgot_password))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.dialog_btn_cancel_forgot_password)) { it, _ ->
                it.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_btn_action_forgot_password)) { it, _ ->
                resetPassword()
                it.dismiss()
            }
            .create()
        dialog.show()
    }

    private fun showNeedToAddFingerprintDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_fingerprint_not_enrolled))
            .setMessage(getString(R.string.dialog_message_fingerprint_not_enrolled))
            .setPositiveButton(
                getString(R.string.dialog_btn_action_fingerprint_not_enrolled)
            ) { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    startActivity(Intent(Settings.ACTION_FINGERPRINT_ENROLL))
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
            }
            .create()
        dialog.show()
    }
}
