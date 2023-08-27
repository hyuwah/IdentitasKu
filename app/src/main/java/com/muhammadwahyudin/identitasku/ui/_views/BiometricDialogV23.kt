package com.muhammadwahyudin.identitasku.ui._views

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.biometric.BiometricCallback
import com.muhammadwahyudin.identitasku.databinding.BottomSheetBiometricBinding

class BiometricDialogV23(
    context: Context,
    biometricCallback: BiometricCallback
) : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {

    private var biometricCallback: BiometricCallback? = biometricCallback

    private var binding: BottomSheetBiometricBinding? = null

    init {
        setDialogView()
    }

    private fun setDialogView() {
        BottomSheetBiometricBinding.inflate(layoutInflater).let {
            binding = it
            setContentView(it.root)
        }
        binding?.btnCancel?.setOnClickListener {
            dismiss()
            biometricCallback?.onAuthenticationCancelled()
        }
        updateLogo()
    }

    fun setTitle(title: String) {
        binding?.itemTitle?.text = title
    }

    fun updateStatus(status: String) {
        binding?.itemStatus?.text = status
    }

    fun setSubtitle(subtitle: String) {
        binding?.itemSubtitle?.text = subtitle
    }

    fun setDescription(description: String) {
        binding?.itemDescription?.text = description
    }

    fun setButtonText(negativeButtonText: String) {
        binding?.btnCancel?.text = negativeButtonText
    }

    private fun updateLogo() {
        try {
            val drawable = context.packageManager.getApplicationIcon(context.packageName)
            binding?.imgLogo?.setImageDrawable(drawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}