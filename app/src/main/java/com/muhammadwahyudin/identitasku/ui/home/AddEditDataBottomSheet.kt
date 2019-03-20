package com.muhammadwahyudin.identitasku.ui.home

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui._views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_add_edit_data.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.act
import timber.log.Timber

class AddEditDataBottomSheet : RoundedBottomSheetDialogFragment() {

    companion object {
        const val ADD = 0
        const val EDIT = 1

        fun newInstance(type: Int, dataWithType: DataWithDataType? = null): AddEditDataBottomSheet {
            val args = Bundle()
            args.putInt("TYPE", type)
            args.putParcelable("DATA", dataWithType)
            val fragment = AddEditDataBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    private var DATA: DataWithDataType? = null
    private var TYPE: Int = 0
    var selectedDataTypeId: Int = -1
    var selectedDataTypeName: String = ""
    var dataInput: String = ""
    private var attr1Input: String = ""
    private var attr2Input: String = ""
    private var attr3Input: String = ""
    private var attr4Input: String = ""
    private var attr5Input: String = ""
    var isDataModified = false
    lateinit var aty: HomeActivity

    private lateinit var parent_view: CoordinatorLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            TYPE = it.getInt("TYPE", 0)
            DATA = it.getParcelable("DATA")
        }
        aty = activity as HomeActivity
        parent_view = act.find(R.id.parent_home_activity)
        Timber.d("TYPE: $TYPE\nDATA: $DATA")
        return inflater.inflate(R.layout.bottom_sheet_add_edit_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (TYPE) {
            ADD -> {
                tv_title.text = "Add New Data"
                til_default.isEnabled = false
                setupAddDataTypeSpinner()
                btn_save.setOnClickListener {
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
                    Snackbar.make(parent_view, "$selectedDataTypeName successfully added", Snackbar.LENGTH_SHORT).show()
                    this@AddEditDataBottomSheet.dismiss()
                }
            }
            EDIT -> {
                tv_title.text = "Edit Data"
                setupEditDataTypeSpinner()
                btn_save.setOnClickListener {
                    dataInput = if (dataInput.isNotEmpty()) dataInput else DATA!!.value
                    attr1Input = if (attr1Input.isNotEmpty()) attr1Input else DATA!!.attr1 ?: attr1Input
                    attr2Input = if (attr2Input.isNotEmpty()) attr2Input else DATA!!.attr2 ?: attr2Input
                    attr3Input = DATA!!.attr3 ?: attr3Input
                    attr4Input = DATA!!.attr4 ?: attr4Input
                    attr5Input = DATA!!.attr5 ?: attr5Input
                    val data = Data(
                        DATA!!.typeId,
                        dataInput,
                        attr1Input,
                        attr2Input,
                        attr3Input,
                        attr4Input,
                        attr5Input
                    )
                    data.id = DATA!!.id
                    aty.viewModel.updateData(data)
                    Snackbar.make(parent_view, "${DATA!!.typeName} successfully modified", Snackbar.LENGTH_SHORT).show()
                    this@AddEditDataBottomSheet.dismiss()
                }
            }
        }

        btn_save.isEnabled = false

        val defaultEditText = til_default.editText!!
        defaultEditText.doOnTextChanged { text, _, _, _ ->
            btn_save.isEnabled = !text.isNullOrEmpty() && selectedDataTypeId != -1
            dataInput = text.toString()
            checkIfDataIsModified(dataInput, DATA?.value)
        }

        til_attr1.editText!!.doOnTextChanged { text, _, _, _ ->
            attr1Input = text.toString()
            checkIfDataIsModified(attr1Input, DATA?.attr1)
        }
        til_attr2.editText!!.doOnTextChanged { text, _, _, _ ->
            attr2Input = text.toString()
            checkIfDataIsModified(attr2Input, DATA?.attr2)
        }
        til_attr3.editText!!.doOnTextChanged { text, _, _, _ ->
            attr3Input = text.toString()
            checkIfDataIsModified(attr3Input, DATA?.attr3)
        }
        til_attr4.editText!!.doOnTextChanged { text, _, _, _ ->
            attr4Input = text.toString()
            checkIfDataIsModified(attr4Input, DATA?.attr4)
        }
        til_attr5.editText!!.doOnTextChanged { text, _, _, _ ->
            attr5Input = text.toString()
            checkIfDataIsModified(attr5Input, DATA?.attr5)
        }

    }

    // Check if data modified on edit mode
    fun checkIfDataIsModified(text: String, value: String?) {
        if (TYPE == EDIT && text != value && !text.isEmpty()) {
            btn_save.isEnabled = true
        }
    }

    private fun setupEditDataTypeSpinner() {
        val dataTypeStr = arrayListOf<String>()
        dataTypeStr.add(DATA!!.typeName)
        val dataTypeAdapter = ArrayAdapter<String>(aty, android.R.layout.simple_spinner_item, dataTypeStr)
        dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_data_type.setTitle("Category")
        spinner_data_type.adapter = dataTypeAdapter
        spinner_data_type.onSearchableItemClicked(DATA!!.typeName, 0)
        spinner_data_type.isEnabled = false
        updateUIBySelectedType(DATA!!.typeId)
    }

    private fun setupAddDataTypeSpinner() {
        val dataTypeStr = arrayListOf<String>()
        val dataType = ArrayList<DataType>()
        // Fetch All data type to spinner
        aty.viewModel.getAllDataType().observe(this, Observer {
            it.toCollection(dataType)
            dataTypeStr.clear()
            Timber.d("All DataType $it")
            it.forEach { dataTypeItem ->
                dataTypeStr.add(dataTypeItem.name)
//                Timber.d("DataType " + dataTypeItem.toString())
            }
            Timber.d("DataType Raw $dataTypeStr")

            // Filter unique data type that exists on data table
            aty.viewModel.getAllExistingUniqueType().observe(this, Observer { existingUniqueType ->
                Timber.d("existingUniqueType $existingUniqueType")
                existingUniqueType.forEach { item ->
                    dataTypeStr.remove(item.name)
                }
                Timber.d("DataType Filtered $dataTypeStr")
            })
        })

        val dataTypeAdapter = ArrayAdapter<String>(aty, android.R.layout.simple_spinner_item, dataTypeStr)
        dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_data_type.setTitle("Category")
        spinner_data_type.setPositiveButton("Close")
        spinner_data_type.adapter = dataTypeAdapter
        spinner_data_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dataType.find { it.name == dataTypeAdapter.getItem(position) }?.let { selectedDataTypeId = it.id }
                til_default.isEnabled = true
                btn_save.isEnabled = dataInput.isNotEmpty()
                updateUIBySelectedType(selectedDataTypeId)
            }
        }
    }

    fun updateUIBySelectedType(type: Int) {
        // Default
        til_attr1.visibility = View.GONE
        til_attr2.visibility = View.GONE
        til_attr3.visibility = View.GONE
        til_attr4.visibility = View.GONE
        til_attr5.visibility = View.GONE
        attr1Input = ""
        attr2Input = ""
        attr3Input = ""
        attr4Input = ""
        attr5Input = ""

        DATA?.let { til_default.editText?.setText(it.value) }
        if (spinner_data_type.selectedItem != null) {
            selectedDataTypeName = spinner_data_type.selectedItem as String
        }

        when (type) {
            Constants.TYPE_ALAMAT -> {
                til_default.editText?.inputType = InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                        InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
                til_attr1.visibility = View.VISIBLE
                til_attr1.hint = "Keterangan"
                til_attr1.editText?.inputType = InputType.TYPE_CLASS_TEXT

                DATA?.let {
                    til_attr1.editText?.setText(it.attr1)
                }
            }
            Constants.TYPE_KTP -> {
                til_default.editText?.inputType = InputType.TYPE_CLASS_NUMBER
            }
            Constants.TYPE_EMAIL -> {
                til_default.editText?.inputType = InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            Constants.TYPE_HANDPHONE -> {
                til_default.editText?.inputType = InputType.TYPE_CLASS_PHONE
                til_attr1.visibility = View.VISIBLE
                til_attr1.hint = "Keterangan"
                til_attr1.editText?.inputType = InputType.TYPE_CLASS_TEXT
                til_attr2.visibility = View.VISIBLE
                til_attr2.hint = "Provider"
                til_attr2.editText?.inputType = InputType.TYPE_CLASS_TEXT

                DATA?.let {
                    til_attr1.editText?.setText(it.attr1)
                    til_attr2.editText?.setText(it.attr2)
                }
            }
            Constants.TYPE_REK_BANK -> {
                til_default.editText?.inputType = InputType.TYPE_CLASS_NUMBER
                til_attr1.visibility = View.VISIBLE
                til_attr1.hint = "Keterangan"
                til_attr1.editText?.inputType = InputType.TYPE_CLASS_TEXT
                til_attr2.visibility = View.VISIBLE
                til_attr2.hint = "Bank"
                til_attr2.editText?.inputType = InputType.TYPE_CLASS_TEXT

                DATA?.let {
                    til_attr1.editText?.setText(it.attr1)
                    til_attr2.editText?.setText(it.attr2)
                }

            }
            else -> {
                til_default.editText?.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
    }


}