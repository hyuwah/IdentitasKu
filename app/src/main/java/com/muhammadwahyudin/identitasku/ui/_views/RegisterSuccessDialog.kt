package com.muhammadwahyudin.identitasku.ui._views

import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.utils.lottieAnimationView
import org.jetbrains.anko.*

private const val LOTTIEVIEW_HEIGHT = 128
private const val LOTTIEVIEW_WIDTH = 128

class RegisterSuccessDialog(ui: AnkoContext<View>) {

    var dialog: DialogInterface
    lateinit var continueButton: Button

    init {
        with(ui) {
            dialog = alert {
                titleResource = R.string.dialog_title_register_success
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
                        continueButton = themedButton(R.style.Widget_MaterialComponents_Button_TextButton_Dialog) {
                            backgroundColorResource = R.color.mtrl_btn_transparent_bg_color
                            text = "Continue"
                        }.lparams(wrapContent, wrapContent) {
                            alignParentRight()
                            below(R.id.lav_register_success)
                            rightMargin = dip(12)
                            bottomMargin = dip(8)
                        }
                    }

                }
                isCancelable = false
            }.show()
        }
    }
}
