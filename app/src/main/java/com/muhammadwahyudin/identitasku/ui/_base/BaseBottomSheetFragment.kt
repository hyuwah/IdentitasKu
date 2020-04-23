package com.muhammadwahyudin.identitasku.ui._base

import android.app.Dialog
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muhammadwahyudin.identitasku.R
import com.google.android.material.R as MaterialR

open class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    protected var isCancellable = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme).apply {
            setOnShowListener {
                val bottomSheet =
                    (it as BottomSheetDialog)
                        .findViewById<FrameLayout>(MaterialR.id.design_bottom_sheet)
                bottomSheet?.let { view ->
                    BottomSheetBehavior.from(view).run {
                        skipCollapsed = true
                        isCancelable = this@BaseBottomSheetFragment.isCancellable
                        peekHeight = bottomSheet.height
                        state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, this::class.java.simpleName)
    }
}