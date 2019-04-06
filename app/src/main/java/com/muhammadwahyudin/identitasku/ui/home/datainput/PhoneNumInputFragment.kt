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
import kotlinx.android.synthetic.main.data_input_phonenum_fragment.*
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener

class PhoneNumInputFragment : BaseDataInputFragment<HomeActivity>() {
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.data_input_phonenum_fragment, container, false)
    }

    override fun setupInputUI() {
        dataOnTextChanged(
            til_phonenum_data.editText,
            btn_save,
            { newText -> dataInput = newText; dataInput },
            _data?.value
        )
        dataOnTextChanged(
            til_phonenum_ket.editText,
            btn_save,
            { newText -> attr1Input = newText; attr1Input },
            _data?.attr1,
            true
        )

        spinner_phonenum_provider.adapter =
            ArrayAdapter(act, android.R.layout.simple_spinner_dropdown_item, InputDataset.PHONENUM_PROVIDERS)
        spinner_phonenum_provider.onItemSelectedListener {
            onItemSelected { adapterView, view, i, l ->
                attr2Input = adapterView?.getItemAtPosition(i).toString()
                checkIfDataIsModified(btn_save, attr2Input, _data?.attr2, true)
            }
        }
    }

    override fun setupUIwithData(data: DataWithDataType) {
        til_phonenum_data.editText?.setText(data.value)
        til_phonenum_ket.editText?.setText(data.attr1)
        var selectedPos = InputDataset.PHONENUM_PROVIDERS.indexOf(data.attr2)
        if (selectedPos != -1)
            spinner_phonenum_provider.setSelection(selectedPos)
        btn_save.isEnabled = false
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

}