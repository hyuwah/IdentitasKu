package com.muhammadwahyudin.identitasku.biometric

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.muhammadwahyudin.identitasku.utils.BiometricUtils


class BiometricManager private constructor(biometricBuilder: BiometricBuilder) : BiometricManagerV23() {

    init {
        this.context = biometricBuilder.context
        this.title = biometricBuilder.title
        this.subtitle = biometricBuilder.subtitle
        this.description = biometricBuilder.description
        this.negativeButtonText = biometricBuilder.negativeButtonText
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun authenticate(@NonNull biometricCallback: BiometricCallback) {

        if (!BiometricUtils.isSdkVersionSupported) {
//            biometricCallback.onSdkVersionNotSupported()
        }

        if (!BiometricUtils.isPermissionGranted(context!!)) {
            biometricCallback.onBiometricAuthenticationPermissionNotGranted()
        }

        if (!BiometricUtils.isHardwareSupported(context!!)) {
//            biometricCallback.onBiometricAuthenticationNotSupported()
        }

        if (!BiometricUtils.isFingerprintAvailable(context!!)) {
            biometricCallback.onBiometricAuthenticationNotAvailable()
        }

        displayBiometricDialog(biometricCallback)
    }


    private fun displayBiometricDialog(biometricCallback: BiometricCallback) {
        if (BiometricUtils.isBiometricPromptEnabled) {
            displayBiometricPrompt(biometricCallback)
        } else {
            displayBiometricPromptV23(biometricCallback)
        }
    }


    @TargetApi(Build.VERSION_CODES.P)
    private fun displayBiometricPrompt(biometricCallback: BiometricCallback) {
        BiometricPrompt.Builder(context!!)
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButton(negativeButtonText, context!!.mainExecutor,
                DialogInterface.OnClickListener { _, _ -> biometricCallback.onAuthenticationCancelled() })
            .build()
            .authenticate(
                CancellationSignal(), context!!.mainExecutor,
                BiometricCallbackV28(biometricCallback)
            )
    }


    class BiometricBuilder(val context: Context) {
        lateinit var title: String
        lateinit var subtitle: String
        lateinit var description: String
        lateinit var negativeButtonText: String

        fun setTitle(@NonNull title: String): BiometricBuilder {
            this.title = title
            return this
        }

        fun setSubtitle(@NonNull subtitle: String): BiometricBuilder {
            this.subtitle = subtitle
            return this
        }

        fun setDescription(@NonNull description: String): BiometricBuilder {
            this.description = description
            return this
        }

        fun setNegativeButtonText(@NonNull negativeButtonText: String): BiometricBuilder {
            this.negativeButtonText = negativeButtonText
            return this
        }

        fun build(): BiometricManager {
            return BiometricManager(this)
        }
    }
}
