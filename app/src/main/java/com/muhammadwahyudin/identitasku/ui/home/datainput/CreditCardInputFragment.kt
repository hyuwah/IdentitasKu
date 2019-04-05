package com.muhammadwahyudin.identitasku.ui.home.datainput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import kotlinx.android.synthetic.main.data_input_cc_fragment.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.findOptional

class CreditCardInputFragment : BaseDataInputFragment<HomeActivity>() {
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.data_input_cc_fragment, container, false)
    }

    override fun setupInputUI() {
        ccv.setIsFlippable(true)
        ccv.find<EditText>(R.id.card_number).doOnTextChanged { text, start, count, after ->
            btn_save.isEnabled = !text.isNullOrEmpty()
            dataInput = text.toString()
            checkIfDataIsModified(btn_save, dataInput, _data?.value)
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
        ccv.cardNumber = data.value
        ccv.expiryDate = data.attr2
        findOptional<EditText>(R.id.cvv_et)?.setText("999")

    }

}