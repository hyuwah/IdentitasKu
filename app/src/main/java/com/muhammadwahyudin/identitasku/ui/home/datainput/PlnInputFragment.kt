package com.muhammadwahyudin.identitasku.ui.home.datainput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.InputDataset
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import kotlinx.android.synthetic.main.data_input_pln_fragment.*

class PlnInputFragment : BaseDataInputFragment() {
    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.data_input_pln_fragment, container, false)
    }

    override fun setupInputUI() {
        dataOnTextChanged(
            til_pln_data.editText,
            btn_save,
            { newText -> dataInput = newText; dataInput },
            _data?.value
        )
        dataOnTextChanged(
            til_pln_ket.editText,
            btn_save,
            { newText -> attr1Input = newText; attr1Input },
            _data?.attr1,
            true
        )

        spinner_pln_provider.adapter =
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                InputDataset.PLN_TYPE
            )
        spinner_pln_provider.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                attr2Input = adapterView?.getItemAtPosition(i).toString()
                checkIfDataIsModified(btn_save, attr2Input, _data?.attr2, true)
            }
        }
    }

    override fun setupAddType() {
        btn_save.setOnClickListener {
            saveAddData()
        }
    }

    override fun setupEditType() {
        btn_save.setOnClickListener {
            saveUpdateData()
        }
    }

    override fun setupUIwithData(data: DataWithDataType) {
        til_pln_data.editText?.setText(data.value)
        til_pln_ket.editText?.setText(data.attr1)
        val selectedPos = InputDataset.PLN_TYPE.indexOf(data.attr2)
        if (selectedPos != -1)
            spinner_pln_provider.setSelection(selectedPos)
        btn_save.isEnabled = false
    }
}