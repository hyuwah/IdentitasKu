package com.muhammadwahyudin.identitasku.ui.datainput.fragment

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.databinding.DataInputCcFragmentBinding
import com.muhammadwahyudin.identitasku.ui._helper.viewBinding
import com.muhammadwahyudin.identitasku.ui.datainput.DataInputViewModelImpl
import com.muhammadwahyudin.identitasku.ui.datainput.contract.FragmentDataInputViewModel
import com.muhammadwahyudin.identitasku.ui.datainput.contract.InputLogic
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.muhammadwahyudin.identitasku.utils.launchActivity

class CreditCardInputFragment : Fragment(R.layout.data_input_cc_fragment) {

    companion object {
        fun newInstance(dataLogic: InputLogic): CreditCardInputFragment {
            return CreditCardInputFragment().apply {
                this.dataLogic = dataLogic
            }
        }
    }

    private var dataLogic: InputLogic? = null
    private val bind by viewBinding(DataInputCcFragmentBinding::bind)
    private val viewModel: FragmentDataInputViewModel by activityViewModels<DataInputViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataLogic?.let { dataLogic ->

            bind.ccv.setIsFlippable(true)

            // Check inmemory data exist or null (ADD or EDIT)
            viewModel.getCurrentData()?.let {
                // set data if edit
                bind.ccv.cardNumber = it.value
                bind.tilGenericKet.editText?.setText(it.attr1)
                bind.ccv.cardName = it.attr2
                bind.ccv.expiryDate = it.attr3
                bind.ccv.findViewById<EditText>(R.id.cvv_et).setText(it.attr4)
            }

            // ontextchange -> fun validation
            bind.ccv.findViewById<EditText>(R.id.card_number).doAfterTextChanged {
                viewModel.checkValueValidation(dataLogic.checkValidation(it.toString()))
            }
            bind.tilGenericKet.editText?.doAfterTextChanged {
                viewModel.setAttribute(attr1 = it.toString().trim())
            }
            bind.ccv.findViewById<EditText>(R.id.card_name).doAfterTextChanged {
                viewModel.setAttribute(attr2 = it.toString().trim())
            }
            bind.ccv.findViewById<EditText>(R.id.expiry_date).doAfterTextChanged {
                viewModel.setAttribute(attr3 = it.toString().trim())
            }
            bind.ccv.findViewById<EditText>(R.id.cvv_et).doAfterTextChanged {
                viewModel.setAttribute(attr4 = it.toString().trim())
            }
        } ?: run {
            requireActivity().launchActivity<HomeActivity>()
            requireActivity().finish()
        }
    }
}