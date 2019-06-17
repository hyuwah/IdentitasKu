package com.muhammadwahyudin.identitasku.utils

import android.view.View
import android.view.ViewManager
import com.airbnb.lottie.LottieAnimationView
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.lottieAnimationView() = lottieAnimationView {}
inline fun ViewManager.lottieAnimationView(theme: Int = 0, init: LottieAnimationView.() -> Unit) =
    ankoView({ LottieAnimationView(it) }, theme, init)

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}