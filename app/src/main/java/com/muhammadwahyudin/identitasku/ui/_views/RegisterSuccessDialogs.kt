package com.muhammadwahyudin.identitasku.ui._views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.muhammadwahyudin.identitasku.databinding.DialogRegisterSuccessBinding

class RegisterSuccessDialogs : DialogFragment() {

    private var _binding: DialogRegisterSuccessBinding? = null
    private val binding get() = _binding!!

    private var onClick: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRegisterSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            onClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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