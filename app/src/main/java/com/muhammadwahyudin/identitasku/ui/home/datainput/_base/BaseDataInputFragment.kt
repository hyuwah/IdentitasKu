package com.muhammadwahyudin.identitasku.ui.home.datainput._base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.AddEditDataBottomSheet
import com.muhammadwahyudin.identitasku.ui.home.AddEditDataBottomSheet.Companion.EDIT
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.muhammadwahyudin.identitasku.ui.home.HomeViewModel
import org.jetbrains.anko.find

abstract class BaseDataInputFragment<T : HomeActivity> : Fragment() {

    companion object {
        const val DATA_PARCEL_KEY = "data_parcel_key"
    }

    interface IDataInput {
    }

    var _type: Int = 0
    var _data: DataWithDataType? = null
    lateinit var act: HomeActivity
    lateinit var parentDialog: AddEditDataBottomSheet
    lateinit var parent_view: View
    lateinit var parentViewModel: HomeViewModel

    // DATA ATTR
    var dataInput = ""
    var attr1Input = ""
    var attr2Input = ""
    var attr3Input = ""
    var attr4Input = ""
    var attr5Input = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflateView(inflater, container, savedInstanceState)
        _data = arguments?.getParcelable(DATA_PARCEL_KEY)
        act = activity as T
        parentDialog = (parentFragment as AddEditDataBottomSheet)
        _type = parentDialog.TYPE
        parent_view = act.find(R.id.parent_home_activity)
        parentViewModel = act.viewModel
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

    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    abstract fun setupEditType()
    abstract fun setupAddType()
    abstract fun setupUIwithData(data: DataWithDataType)
    abstract fun setupInputUI()

    // Check if data modified on edit mode
    fun checkIfDataIsModified(buttonSave: View, text: String, value: String?, isOptional: Boolean = false) {
        if (_type == EDIT && text != value && (!text.isEmpty() || isOptional)) {
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
        Snackbar.make(parent_view, "Nomor KTP successfully added", Snackbar.LENGTH_SHORT).show()
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
        Snackbar.make(parent_view, "Nomor KTP successfully updated", Snackbar.LENGTH_SHORT).show()
        parentDialog.dismiss()
    }

    fun dataOnTextChanged(
        textInputLayout: TextInputLayout,
        buttonSave: View,
        dataAssignment: (newText: String) -> String,
        oldData: String?,
        isOptional: Boolean = false
    ) {
        textInputLayout.editText?.doOnTextChanged { text, start, count, after ->
            buttonSave.isEnabled = !text.isNullOrEmpty()
            dataAssignment(text.toString())
            checkIfDataIsModified(buttonSave, dataAssignment(text.toString()), oldData, isOptional)
        }
    }

}