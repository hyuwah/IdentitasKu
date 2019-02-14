package com.muhammadwahyudin.identitasku.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.ui._views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_add_data.*
import org.jetbrains.anko.toast
import timber.log.Timber

class AddDataBottomSheet : RoundedBottomSheetDialogFragment() {

    var selectedDataType: Int = -1
    var dataInput: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataTypeSpinner()

        btn_save.isEnabled = false

        val defaultEditText = til_default.editText!!
        defaultEditText.doOnTextChanged { text, start, count, after ->
            btn_save.isEnabled = !text.isNullOrEmpty() && selectedDataType != -1
            dataInput = text.toString()
        }

        btn_save.setOnClickListener {
            activity?.toast("Data Saved\nType $selectedDataType - $dataInput")
            this@AddDataBottomSheet.dismiss()
        }
    }

    private fun setupDataTypeSpinner() {
        val dataTypeStr = arrayListOf<String>()
        var dataType = ArrayList<DataType>()
        (activity as HomeActivity).viewModel.getAllDataType().observe(this, Observer {
            it.toCollection(dataType)
            dataTypeStr.clear()
            it.forEach { dataTypeItem ->
                dataTypeStr.add(dataTypeItem.name)
                Timber.d("DataType " + dataTypeItem.toString())
            }
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
                dataType.find { it -> it.name == dataTypeAdapter.getItem(position) }?.let { selectedDataType = it.id }
                activity?.toast("Selected ${dataTypeAdapter.getItem(position)} \n Id $selectedDataType")
                btn_save.isEnabled = dataInput.isNotEmpty()
            }
        }
    }


}