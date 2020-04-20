package com.muhammadwahyudin.identitasku.ui._views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.muhammadwahyudin.identitasku.R
import kotlinx.android.synthetic.main.dialog_register_success.*

class RegisterSuccessDialogs : DialogFragment() {

    private var onClick: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_register_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            onClick()
        }
    }

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun show(manager: FragmentManager, onClick: () -> Unit = {}) {
        this.onClick = onClick
        show(manager, this.javaClass.simpleName)
    }
}