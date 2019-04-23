package com.muhammadwahyudin.identitasku.ui._helper

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import co.mobiwise.materialintro.shape.Focus
import co.mobiwise.materialintro.shape.FocusGravity
import co.mobiwise.materialintro.shape.ShapeType
import co.mobiwise.materialintro.view.MaterialIntroView
import com.muhammadwahyudin.identitasku.R

/**
 * Singleton object for tutorial / showcase view related methods
 */
object TutorialHelper {

    /**
     * First time add / edit fab tutorial
     * @param activity
     * @param view Add / Edit FAB
     */
    fun initAddEditFab(activity: Activity, view: View) {
        MaterialIntroView.Builder(activity)
            .enableDotAnimation(false)
            .setFocusGravity(FocusGravity.CENTER)
            .setFocusType(Focus.MINIMUM)
            .setDelayMillis(1000)
            .setIdempotent(true)
            .enableFadeAnimation(true)
            .performClick(true)
            .setInfoText(activity.getString(R.string.tutorial_btn_add_data))
            .setShape(ShapeType.CIRCLE)
            .setTarget(view)
            .setUsageId("bsc_add_edit_fab") //THIS SHOULD BE UNIQUE ID
            .show()
    }

    /**
     * First time data item added tutorial
     * @param activity
     * @param view RecyclerView
     */
    fun initFirstDataItem(activity: Activity, view: RecyclerView) {
        MaterialIntroView.Builder(activity)
            .enableDotAnimation(true)
            .setFocusGravity(FocusGravity.CENTER)
            .setFocusType(Focus.MINIMUM)
            .setDelayMillis(500)
            .setIdempotent(true)
            .enableFadeAnimation(true)
            .setInfoText(activity.getString(R.string.tutorial_item_list))
            .setShape(ShapeType.RECTANGLE)
            .setTarget(view.getChildAt(0))
            .setUsageId("bsc_rv_item") //THIS SHOULD BE UNIQUE ID
            .show()
    }

    /**
     * Clear shared preferences created by BubbleShowCasePrefs
     * @param activity
     */
    fun resetTutorial(activity: Activity) {
        val preferences = activity.getSharedPreferences("material_intro_preferences", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.commit()
    }
}