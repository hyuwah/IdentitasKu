package com.muhammadwahyudin.identitasku.ui._views

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.biometric.BiometricCallback
import kotlinx.android.synthetic.main.bottom_sheet_biometric.*

class BiometricDialogV23(
    context: Context,
    biometricCallback: BiometricCallback
) : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {

    private var biometricCallback: BiometricCallback? = biometricCallback

    init {
        setDialogView()
    }

    private fun setDialogView() {
        val bottomSheetView = layoutInflater.inflate(
            R.layout.bottom_sheet_biometric, null
        )
        setContentView(bottomSheetView)
        btn_cancel.setOnClickListener {
            dismiss()
            biometricCallback?.onAuthenticationCancelled()
        }
        updateLogo()
    }

    fun setTitle(title: String) {
        item_title.text = title
    }

    fun updateStatus(status: String) {
        item_status.text = status
    }

    fun setSubtitle(subtitle: String) {
        item_subtitle.text = subtitle
    }

    fun setDescription(description: String) {
        item_description.text = description
    }

    fun setButtonText(negativeButtonText: String) {
        btn_cancel.text = negativeButtonText
    }

    private fun updateLogo() {
        try {
            val drawable = context.packageManager.getApplicationIcon(context.packageName)
            img_logo.setImageDrawable(drawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}