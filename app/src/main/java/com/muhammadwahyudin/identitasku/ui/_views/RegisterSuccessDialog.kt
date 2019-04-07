package com.muhammadwahyudin.identitasku.ui._views

import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.core.content.ContextCompat
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.utils.lottieAnimationView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.Appcompat

private const val LOTTIEVIEW_HEIGHT = 128
private const val LOTTIEVIEW_WIDTH = 128

class RegisterSuccessDialog(ui: AnkoContext<View>) {

    var dialog: DialogInterface
    lateinit var onPositiveBtnClick: () -> Unit
    init {
        with(ui) {
            dialog = alert(Appcompat) {
                titleResource = R.string.dialog_title_register_success
                positiveButton(R.string.dialog_btn_ok) {
                    it.dismiss()
                    onPositiveBtnClick()
                }
                this.customView {
                    themedRelativeLayout(R.style.AppTheme) {
                        this.layoutParams = LayoutParams(
                            wrapContent,
                            wrapContent
                        )
                        lottieAnimationView {
                            id = R.id.lav_register_success
                            this.adjustViewBounds = true
                            this.setAnimation(R.raw.success)
                            this.loop(false)
                            this.playAnimation()
                        }.lparams(dip(LOTTIEVIEW_WIDTH), dip(LOTTIEVIEW_HEIGHT)) {
                            centerHorizontally()
                            topMargin = dip(8)
                        }
                        textView(R.string.dialog_message_register_success) {
                            id = R.id.tv_desc_register_success
                            textColor = ContextCompat.getColor(ctx, android.R.color.black)
                        }.lparams(wrapContent, wrapContent) {
                            below(R.id.lav_register_success)
                            rightMargin = dip(20)
                            leftMargin = dip(20)
                        }
                    }
                }
                isCancelable = false
            }.show()
        }
    }
}
