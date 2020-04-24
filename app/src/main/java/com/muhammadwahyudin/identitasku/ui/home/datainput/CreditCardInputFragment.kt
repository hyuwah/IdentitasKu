package com.muhammadwahyudin.identitasku.ui.home.datainput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import kotlinx.android.synthetic.main.data_input_cc_fragment.*

class CreditCardInputFragment : BaseDataInputFragment() {

    private lateinit var etCardNumber: EditText
    private lateinit var etCardholderName: EditText
    private lateinit var etCardExpireDate: EditText
    private lateinit var etCardCvv: EditText

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.data_input_cc_fragment, container, false)
    }

    override fun setupInputUI() {
        (parentDialog.dialog as BottomSheetDialog?)?.behavior?.state =
            BottomSheetBehavior.STATE_EXPANDED

        ccv.setIsFlippable(true)

        etCardNumber = ccv.findViewById(R.id.card_number)
        dataOnTextChanged(
            etCardNumber,
            btn_save,
            { newText -> dataInput = newText; dataInput },
            data?.value
        )

        etCardholderName = ccv.findViewById(R.id.card_name)
        dataOnTextChanged(
            etCardholderName,
            btn_save,
            { newText -> attr2Input = newText; attr2Input },
            data?.attr2,
            true
        )

        etCardExpireDate = ccv.findViewById(R.id.expiry_date)
        dataOnTextChanged(
            etCardExpireDate,
            btn_save,
            { newText -> attr3Input = newText; attr3Input },
            data?.attr3,
            true
        )

        etCardCvv = ccv.findViewById(R.id.cvv_et)
        dataOnTextChanged(
            etCardCvv,
            btn_save,
            { newText -> attr4Input = newText; attr4Input },
            data?.attr4,
            true
        )

        dataOnTextChanged(
            til_cc_ket.editText,
            btn_save,
            { newText -> attr1Input = newText; attr1Input },
            data?.attr1,
            true
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
        ccv.cardNumber = data.value
        ccv.cardName = data.attr2
        ccv.expiryDate = data.attr3
        etCardCvv.setText(data.attr4)
        til_cc_ket.editText?.setText(data.attr1)
        btn_save.isEnabled = false
    }
}