package com.muhammadwahyudin.identitasku.ui.home.datainput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import kotlinx.android.synthetic.main.data_input_generic_1_fragment.*

class Generic1InputFragment : BaseDataInputFragment() {

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.data_input_generic_1_fragment, container, false)
    }

    override fun setupInputUI() {
        til_generic_data.hint = _typeName
        dataOnTextChanged(
            til_generic_data.editText,
            btn_save,
            { txt -> dataInput = txt; dataInput },
            _data?.value
        )
    }

    override fun setupUIwithData(data: DataWithDataType) {
        til_generic_data.editText?.setText(data.value)
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