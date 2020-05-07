package com.muhammadwahyudin.identitasku.ui.datainput.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.databinding.DataInputGenericFragmentBinding
import com.muhammadwahyudin.identitasku.ui._helper.viewBinding
import com.muhammadwahyudin.identitasku.ui.datainput.DataInputViewModelImpl
import com.muhammadwahyudin.identitasku.ui.datainput.contract.FragmentDataInputViewModel
import com.muhammadwahyudin.identitasku.ui.datainput.contract.InputLogic
import com.muhammadwahyudin.identitasku.ui.datainput.contract.ValidationResult
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.muhammadwahyudin.identitasku.utils.launchActivity
import com.muhammadwahyudin.identitasku.utils.setVisible

class GenericInputFragment : Fragment(R.layout.data_input_generic_fragment) {

    companion object {
        fun newInstance(dataLogic: InputLogic): GenericInputFragment {
            return GenericInputFragment().apply {
                this.dataLogic = dataLogic
            }
        }
    }

    private var dataLogic: InputLogic? = null
    private val bind by viewBinding(DataInputGenericFragmentBinding::bind)
    private val viewModel: FragmentDataInputViewModel by activityViewModels<DataInputViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataLogic?.let { dataLogic ->
            bind.tilGenericData.hint = dataLogic.typeName
            bind.tilGenericData.editText?.let { it.inputType = dataLogic.valueInputType }

            // Dropdown / Attribute 2
            dataLogic.attribute2Data()?.let { (hint, list) ->
                bind.tilActvAttribute2.setVisible()
                bind.tilActvAttribute2.hint = hint
                val adapter =
                    ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, list)
                bind.actvAttribute2.setAdapter(adapter)
                bind.actvAttribute2.setOnItemClickListener { adapterView, _, i, _ ->
                    viewModel.setAttribute(attr2 = adapterView.getItemAtPosition(i).toString())
                }
            }

            // Check inmemory data exist or null (ADD or EDIT)
            viewModel.getCurrentData()?.let {
                // set data if edit
                bind.tilGenericData.editText?.setText(it.value)
                bind.tilGenericKet.editText?.setText(it.attr1)
                bind.actvAttribute2.setText(it.attr2, false)
            }

            // ontextchange -> fun validation
            bind.tilGenericData.editText?.doOnTextChanged { text, _, _, _ -> onTextChanged(text) }
            bind.tilGenericKet.editText?.doAfterTextChanged {
                viewModel.setAttribute(attr1 = it.toString().trim())
            }
        } ?: run {
            requireActivity().launchActivity<HomeActivity>()
            requireActivity().finish()
        }
    }

    private fun onTextChanged(text: CharSequence?) {
        text?.let {
            viewModel.hasUnsavedData(text.isNotEmpty())
            val result = dataLogic?.checkValidation(it)
            result?.let {
                viewModel.checkValueValidation(result)
                when (result) {
                    is ValidationResult.Valid -> {
                        bind.tilGenericData.error = null
                        bind.tilGenericData.isErrorEnabled = false
                    }
                    is ValidationResult.Invalid -> {
                        bind.tilGenericData.error = result.errorMessage
                    }
                }
            }
        }
    }
}