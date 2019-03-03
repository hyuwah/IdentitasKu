package com.muhammadwahyudin.identitasku.ui.home

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.ui._views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_add_data.*
import org.jetbrains.anko.toast
import timber.log.Timber

class AddDataBottomSheet : RoundedBottomSheetDialogFragment() {

    var selectedDataTypeId: Int = -1
    var dataInput: String = ""
    var attr1Input: String = ""
    var attr2Input: String = ""
    var attr3Input: String = ""
    var attr4Input: String = ""
    var attr5Input: String = ""
    lateinit var aty: HomeActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_data, container, false)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        aty = activity as HomeActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataTypeSpinner()

        btn_save.isEnabled = false

        val defaultEditText = til_default.editText!!
        defaultEditText.doOnTextChanged { text, start, count, after ->
            btn_save.isEnabled = !text.isNullOrEmpty() && selectedDataTypeId != -1
            dataInput = text.toString()
        }

        til_attr1.editText!!.doOnTextChanged { text, start, count, after -> attr1Input = text.toString() }
        til_attr2.editText!!.doOnTextChanged { text, start, count, after -> attr2Input = text.toString() }
        til_attr3.editText!!.doOnTextChanged { text, start, count, after -> attr3Input = text.toString() }
        til_attr4.editText!!.doOnTextChanged { text, start, count, after -> attr4Input = text.toString() }
        til_attr5.editText!!.doOnTextChanged { text, start, count, after -> attr5Input = text.toString() }

        btn_save.setOnClickListener {
            aty.toast("Data Saved\nType $selectedDataTypeId - $dataInput")
            aty.viewModel.addData(
                Data(
                    selectedDataTypeId,
                    dataInput,
                    attr1Input,
                    attr2Input,
                    attr3Input,
                    attr4Input,
                    attr5Input
                )
            )
            this@AddDataBottomSheet.dismiss()
        }
    }

    private fun setupDataTypeSpinner() {
        val dataTypeStr = arrayListOf<String>()
        var dataType = ArrayList<DataType>()
        // Fetch All data type to spinner
        aty.viewModel.getAllDataType().observe(this, Observer {
            it.toCollection(dataType)
            dataTypeStr.clear()
            Timber.d("All DataType ${it}")
            it.forEach { dataTypeItem ->
                dataTypeStr.add(dataTypeItem.name)
//                Timber.d("DataType " + dataTypeItem.toString())
            }
            Timber.d("DataType Raw ${dataTypeStr}")

            // Filter unique data type that exists on data table
            aty.viewModel.getAllExistingUniqueType().observe(this, Observer { existingUniqueType ->
                Timber.d("existingUniqueType ${existingUniqueType}")
                existingUniqueType.forEach { item ->
                    dataTypeStr.remove(item.name)
                }
                Timber.d("DataType Filtered ${dataTypeStr}")
            })
        })

        var dataTypeAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, dataTypeStr)
        dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_data_type.setTitle("Tipe Data")
        spinner_data_type.setPositiveButton("Close")
        spinner_data_type.adapter = dataTypeAdapter
        spinner_data_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dataType.find { it -> it.name == dataTypeAdapter.getItem(position) }?.let { selectedDataTypeId = it.id }
                aty.toast("Selected ${dataTypeAdapter.getItem(position)} \n Id $selectedDataTypeId")
                btn_save.isEnabled = dataInput.isNotEmpty()
                updateUIBySelectedType(selectedDataTypeId)
            }
        }
    }

    fun updateUIBySelectedType(type: Int) {
        // Default
        til_attr1.visibility = View.GONE

        when (type) {
            Constants.TYPE_ALAMAT -> {
                til_default.editText?.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            }
            Constants.TYPE_KTP -> {
                til_default.editText?.inputType = InputType.TYPE_CLASS_NUMBER
            }
            Constants.TYPE_EMAIL -> {
                til_default.editText?.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            Constants.TYPE_HANDPHONE -> {
                til_default.editText?.inputType = InputType.TYPE_CLASS_PHONE
            }
            Constants.TYPE_REK_BANK -> {
                til_default.editText?.inputType = InputType.TYPE_CLASS_NUMBER
                til_attr1.visibility = View.VISIBLE
                til_attr1.hint = "Nama Bank"
                til_attr1.editText?.inputType = InputType.TYPE_CLASS_TEXT
            }
            else -> {
                til_default.editText?.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
    }


}