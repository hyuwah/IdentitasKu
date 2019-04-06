package com.muhammadwahyudin.identitasku.ui.home.datainput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.InputDataset
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import kotlinx.android.synthetic.main.data_input_bank_acc_fragment.*
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener

class BankAccountInputFragment : BaseDataInputFragment<HomeActivity>() {
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.data_input_bank_acc_fragment, container, false)
    }

    override fun setupEditType() {
        btn_save.setOnClickListener {
            saveUpdateData()
        }
    }

    override fun setupAddType() {
        btn_save.setOnClickListener {
            saveAddData()
        }
    }

    override fun setupUIwithData(data: DataWithDataType) {
        til_rek_bank_data.editText?.setText(data.value)
        til_rek_bank_ket.editText?.setText(data.attr1)
        var selectedPos = InputDataset.BANK_LIST.indexOf(data.attr2)
        if (selectedPos != -1)
            spinner_rek_bank_provider.setSelection(selectedPos)
        btn_save.isEnabled = false
    }

    override fun setupInputUI() {
        dataOnTextChanged(
            til_rek_bank_data.editText,
            btn_save,
            { newText -> dataInput = newText; dataInput },
            _data?.value
        )
        dataOnTextChanged(
            til_rek_bank_ket.editText,
            btn_save,
            { newText -> attr1Input = newText; attr1Input },
            _data?.attr1,
            true
        )
        spinner_rek_bank_provider.setTitle("Bank")
        spinner_rek_bank_provider.setPositiveButton("Close")
        spinner_rek_bank_provider.adapter =
            ArrayAdapter(act, R.layout.support_simple_spinner_dropdown_item, InputDataset.BANK_LIST)
        spinner_rek_bank_provider.onItemSelectedListener {
            onItemSelected { adapterView, view, i, l ->
                attr2Input = adapterView?.getItemAtPosition(i).toString()
                checkIfDataIsModified(btn_save, attr2Input, _data?.attr2, true)
            }
        }
    }
}