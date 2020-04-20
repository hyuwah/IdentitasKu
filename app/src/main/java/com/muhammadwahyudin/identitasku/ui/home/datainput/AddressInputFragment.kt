package com.muhammadwahyudin.identitasku.ui.home.datainput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import kotlinx.android.synthetic.main.data_input_address_fragment.*

// TODO
// Scroll issue on alamat value multiline
class AddressInputFragment : BaseDataInputFragment() {
    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.data_input_address_fragment, container, false)
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
        til_address_data.editText?.setText(data.value)
        til_address_ket.editText?.setText(data.attr1)
        btn_save.isEnabled = false
    }

    override fun setupInputUI() {
        dataOnTextChanged(
            til_address_data.editText,
            btn_save,
            { newText -> dataInput = newText; dataInput },
            _data?.value
        )
        dataOnTextChanged(
            til_address_ket.editText,
            btn_save,
            { newText -> attr1Input = newText; attr1Input },
            _data?.attr1,
            true
        )
    }
}