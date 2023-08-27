package com.muhammadwahyudin.identitasku.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.jakewharton.processphoenix.ProcessPhoenix
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.biometric.BiometricCallback
import com.muhammadwahyudin.identitasku.biometric.BiometricManager
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.db.AppDatabase
import com.muhammadwahyudin.identitasku.databinding.ActivityLoginBinding
import com.muhammadwahyudin.identitasku.ui._helper.TutorialHelper
import com.muhammadwahyudin.identitasku.ui._views.RegisterSuccessDialogs
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.muhammadwahyudin.identitasku.utils.BiometricUtils
import com.muhammadwahyudin.identitasku.utils.Commons
import com.muhammadwahyudin.identitasku.utils.toast
import com.muhammadwahyudin.identitasku.utils.viewBinding
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

/**
 * A register & login screen that offers login via password/fingerprint.
 */
class LoginActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job()
    private val appDatabase by inject<AppDatabase>()

    private var backToExitPressed = false

    private var isRegistered = false
    private var wrongPasswordInputAttempt = 0

    private val binding by viewBinding(ActivityLoginBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        isRegistered = Hawk.contains(Constants.SP_PASSWORD)

        if (BuildConfig.DEBUG)
            binding.btnLogin.setOnLongClickListener {
                val dialog = RegisterSuccessDialogs()
                dialog.show(supportFragmentManager) {
                    dialog.dismiss()
                }
                true
            }

        if (isRegistered) { // Login
            binding.btnLogin.isEnabled = false
            binding.btnLogin.setOnClickListener {
                if (wrongPasswordInputAttempt > 3) {
                    Commons.hideSoftKeyboard(this)
                    showForgotPasswordDialog()
                    wrongPasswordInputAttempt = 0
                } else
                    validateLogin()
            }
        } else { // First open / register
            binding.tvTitle.text = getString(R.string.register_title)
            binding.btnLogin.text = getString(R.string.button_register)
            binding.textView2.visibility = View.GONE
            binding.tilPasswordConfirm.visibility = View.VISIBLE
            binding.tilPasswordConfirm.isEnabled = false
            binding.btnLogin.isEnabled = false
            binding.btnLoginFp.hide()
            binding.btnLogin.setOnClickListener {
                register()
            }
        }

        binding.password.doOnTextChanged { text, _, _, _ ->
            binding.tilPassword.isErrorEnabled = false
            if (!isRegistered) {
                binding.tilPasswordConfirm.isEnabled = !text.isNullOrEmpty()
            } else {
                binding.btnLogin.isEnabled = !text.isNullOrEmpty()
            }
        }
        binding.passwordConfirm.doOnTextChanged { text, _, _, _ ->
            binding.tilPasswordConfirm.isErrorEnabled = false
            if (!isRegistered) {
                binding.btnLogin.isEnabled = !text.isNullOrEmpty()
            }
        }

        binding.btnLoginFp.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (BiometricUtils.isFingerprintAvailable(this))
                    openFingerprintDialog()
                else
                    showNeedToAddFingerprintDialog()
            }
        }

        // Hide login with fingerprint if device has no sensor
        if (!BiometricUtils.isHardwareSupported(this)) {
            binding.textView2.visibility = View.GONE
            binding.btnLoginFp.hide()
        } // Show fingerprint login, if has sensor, has enrolled & has registered
        else if (
            BiometricUtils.isHardwareSupported(this) &&
            BiometricUtils.isFingerprintAvailable(this) &&
            isRegistered
        ) {
            binding.btnLoginFp.performClick()
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
        binding.btnLoginFp.isEnabled = false
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
        val passwordEdt = binding.password.text
        if (!passwordEdt.isNullOrBlank() && passwordEdt.toString() == Hawk.get(Constants.SP_PASSWORD)) {
            goToHomeActivity()
        } else {
            binding.tilPassword.error = getString(R.string.text_hint_login_password_invalid)
            binding.tilPassword.isErrorEnabled = true
            wrongPasswordInputAttempt += 1
        }
    }

    private fun register() {
        val passwordEdt = binding.password.text
        val passwordConfirmEdt = binding.passwordConfirm.text
        when {
            !passwordEdt.isNullOrBlank() && !passwordConfirmEdt.isNullOrBlank() -> {
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
                    binding.tilPasswordConfirm.error =
                        getString(R.string.text_hint_register_password_confirmation_not_match)
                    binding.tilPasswordConfirm.isErrorEnabled = true
                }
            }
            passwordEdt.isNullOrBlank() -> {
                binding.tilPassword.error = getString(R.string.text_hint_register_password_empty)
                binding.tilPassword.isErrorEnabled = true
            }
            passwordEdt.isNotBlank() && passwordConfirmEdt.isNullOrBlank() -> {
                binding.tilPasswordConfirm.error =
                    getString(R.string.text_hint_register_password_confirmation_empty)
                binding.tilPasswordConfirm.isErrorEnabled = true
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
