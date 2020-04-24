package com.muhammadwahyudin.identitasku.ui.home.datainput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import kotlinx.android.synthetic.main.data_input_generic_2_fragment.*

class Generic2InputFragment : BaseDataInputFragment() {

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.data_input_generic_2_fragment, container, false)
    }

    override fun setupInputUI() {
        til_generic_data.hint = typeName
        dataOnTextChanged(
            til_generic_data.editText,
            btn_save,
            { txt -> dataInput = txt; dataInput },
            data?.value
        )
        dataOnTextChanged(
            til_generic_ket.editText,
            btn_save,
            { txt -> attr1Input = txt; attr1Input },
            data?.attr1
        )
    }

    override fun setupUIwithData(data: DataWithDataType) {
        til_generic_data.editText?.setText(data.value)
        til_generic_ket.editText?.setText(data.attr1)
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