package com.muhammadwahyudin.identitasku.ui.home.datainput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import kotlinx.android.synthetic.main.data_input_generic_2_fragment.*

class Generic2InputFragment : BaseDataInputFragment<HomeActivity>(), BaseDataInputFragment.IDataInput {

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.data_input_generic_2_fragment, container, false)
    }

    override fun setupInputUI() {
        til_generic_data.hint = _typeName
        til_generic_data.editText?.doOnTextChanged { text, start, count, after ->
            btn_save.isEnabled = !text.isNullOrEmpty()
            dataInput = text.toString()
            checkIfDataIsModified(btn_save, dataInput, _data?.value)
        }
        til_generic_ket.editText?.doOnTextChanged { text, start, count, after ->
            btn_save.isEnabled = !text.isNullOrEmpty()
            attr1Input = text.toString()
            checkIfDataIsModified(btn_save, attr1Input, _data?.attr1)
        }
    }

    override fun setupUIwithData(data: DataWithDataType) {
        til_generic_data.editText?.setText(data.value)
        til_generic_ket.editText?.setText(data.attr1)
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