package com.muhammadwahyudin.identitasku.ui.home.datainput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import kotlinx.android.synthetic.main.data_input_email_fragment.*

class EmailInputFragment : BaseDataInputFragment() {
    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.data_input_email_fragment, container, false)
    }

    override fun setupInputUI() {
        dataOnTextChanged(
            til_email_data.editText,
            btn_save,
            { newText -> dataInput = newText; dataInput },
            data?.value
        )
        dataOnTextChanged(
            til_email_ket.editText,
            btn_save,
            { newText -> attr1Input = newText; attr1Input },
            data?.attr1
        )
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
        til_email_data.editText?.setText(data.value)
        til_email_ket.editText?.setText(data.attr1)
        btn_save.isEnabled = false
    }
}