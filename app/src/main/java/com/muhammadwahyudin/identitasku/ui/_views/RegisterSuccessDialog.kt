package com.muhammadwahyudin.identitasku.ui._views

import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.LinearLayout
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
                    themedLinearLayout(R.style.AppTheme_NoActionBar) {
                        this.layoutParams = LayoutParams(
                            wrapContent,
                            wrapContent
                        )
                        this.orientation = LinearLayout.VERTICAL
                        this.gravity = Gravity.CENTER
                        lottieAnimationView {
                            this.layoutParams = LayoutParams(dip(LOTTIEVIEW_WIDTH), dip(LOTTIEVIEW_HEIGHT))
                            this.adjustViewBounds = true
                            this.setAnimation(R.raw.success)
                            this.loop(false)
                            this.playAnimation()
                        }
                        continueButton = themedButton(R.style.Widget_MaterialComponents_Button_TextButton_Dialog) {
                            backgroundColorResource = R.color.mtrl_btn_transparent_bg_color
                            text = "Continue"
                        }
                    }

                }
                isCancelable = false
            }.show()
        }
    }
}
