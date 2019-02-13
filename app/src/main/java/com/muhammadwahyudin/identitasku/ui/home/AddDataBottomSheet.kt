package com.muhammadwahyudin.identitasku.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.ui._views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_add_data.*
import org.jetbrains.anko.toast

class AddDataBottomSheet : RoundedBottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataTypeStr = arrayListOf<String>()
        (activity as HomeActivity).viewModel.getAllDataType().observe(this, Observer {
            dataTypeStr.clear()
            it.forEach { dataTypeItem ->
                dataTypeStr.add(dataTypeItem.name)
            }
        })

        var dataTypeAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, dataTypeStr)
        dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_data_type.setTitle("Tipe Data")
        spinner_data_type.setPositiveButton("Close")
        spinner_data_type.adapter = dataTypeAdapter
        spinner_data_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity?.toast("Selected ${dataTypeAdapter.getItem(position)} ")
            }
        }
    }


}