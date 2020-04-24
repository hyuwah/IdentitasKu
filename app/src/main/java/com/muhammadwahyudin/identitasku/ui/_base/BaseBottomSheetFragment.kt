package com.muhammadwahyudin.identitasku.ui._base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muhammadwahyudin.identitasku.R
import com.google.android.material.R as MaterialR

abstract class BaseBottomSheetFragment<Binding : ViewBinding>(
    val bindingInflate: (i: LayoutInflater, c: ViewGroup?, atr: Boolean) -> Binding
) : BottomSheetDialogFragment() {

    private var _binding: Binding? = null
    protected lateinit var binding: Binding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflate(inflater, container, false)
        binding = _binding!!
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, this::class.java.simpleName)
    }
}