package com.muhammadwahyudin.identitasku.ui.home.datainput._base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.AddEditDataBottomSheet
import com.muhammadwahyudin.identitasku.ui.home.AddEditDataBottomSheet.Companion.EDIT
import com.muhammadwahyudin.identitasku.ui.home.HomeViewModel

abstract class BaseDataInputFragment : Fragment() {

    companion object {
        const val DATA_PARCEL_KEY = "data_parcel_key"
        const val TYPE_NAME_KEY = "type_name_key"
    }

    var _type: Int = 0
    var _data: DataWithDataType? = null
    var _typeName: String? = null
    lateinit var parentDialog: AddEditDataBottomSheet
    private lateinit var parent_view: View
    private val parentViewModel: HomeViewModel by activityViewModels()

    // DATA ATTR
    var dataInput = ""
    var attr1Input = ""
    var attr2Input = ""
    var attr3Input = ""
    var attr4Input = ""
    var attr5Input = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflateView(inflater, container, savedInstanceState)
        _data = arguments?.getParcelable(DATA_PARCEL_KEY)
        _typeName = arguments?.getString(TYPE_NAME_KEY)
        parentDialog = (parentFragment as AddEditDataBottomSheet)
        _type = parentDialog.type
        parent_view = requireActivity().findViewById(R.id.parent_home_activity)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupInputUI()
        when (_type) {
            AddEditDataBottomSheet.ADD -> {
                setupAddType()
            }
            AddEditDataBottomSheet.EDIT -> {
                setupEditType()
                _data?.let {
                    setupUIwithData(it)
                }
            }
        }
    }

    abstract fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    abstract fun setupInputUI()
    abstract fun setupAddType()
    abstract fun setupEditType()
    abstract fun setupUIwithData(data: DataWithDataType)

    // Check if data modified on edit mode
    fun checkIfDataIsModified(
        buttonSave: View,
        text: String,
        value: String?,
        isOptional: Boolean = false
    ) {
        if (_type == EDIT && text != value && (text.isNotEmpty() || isOptional)) {
            buttonSave.isEnabled = true
        }
    }

    fun saveAddData() {
        parentViewModel.addData(
            Data(
                parentDialog.selectedDataTypeId,
                dataInput,
                attr1Input,
                attr2Input,
                attr3Input,
                attr4Input,
                attr5Input
            )
        )
        Snackbar.make(parent_view, "$_typeName successfully added", Snackbar.LENGTH_SHORT).show()
        parentDialog.dismiss()
    }

    fun saveUpdateData() {
        val modifiedData = Data(
            _data!!.typeId,
            dataInput,
            attr1Input,
            attr2Input,
            attr3Input,
            attr4Input,
            attr5Input
        )
        modifiedData.id = _data!!.id
        parentViewModel.updateData(modifiedData)
        Snackbar
            .make(parent_view, "${_data?.typeName} successfully updated", Snackbar.LENGTH_SHORT)
            .show()
        parentDialog.dismiss()
    }

    inline fun dataOnTextChanged(
        editText: EditText?,
        buttonSave: View,
        crossinline dataAssignment: (newText: String) -> String,
        oldData: String?,
        isOptional: Boolean = false
    ) {
        editText?.doOnTextChanged { text, start, count, after ->
            buttonSave.isEnabled = !text.isNullOrEmpty()
            dataAssignment(text.toString())
            checkIfDataIsModified(buttonSave, dataAssignment(text.toString()), oldData, isOptional)
        }
    }
}