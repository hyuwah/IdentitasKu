package com.muhammadwahyudin.identitasku.ui.datainput

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.ui._base.BaseViewModel
import com.muhammadwahyudin.identitasku.ui.datainput.contract.DataInputViewModel
import com.muhammadwahyudin.identitasku.ui.datainput.contract.FragmentDataInputViewModel
import com.muhammadwahyudin.identitasku.ui.datainput.contract.ValidationResult
import com.muhammadwahyudin.identitasku.utils.ioLaunch

class DataInputViewModelImpl(private val repo: IAppRepository) : BaseViewModel(),
    DataInputViewModel, FragmentDataInputViewModel {

    private val _isSaveButtonEnabled = MutableLiveData(false)
    override val isSaveButtonEnabled = _isSaveButtonEnabled as LiveData<Boolean>

    private val _isOperationSuccess = MutableLiveData(false to -1)
    override val isOperationSuccess = _isOperationSuccess as LiveData<Pair<Boolean, Int>>

    private val hasUnsavedData = MutableLiveData(false)

    private lateinit var currentData: Data
    private var currentMode: Int = -1
    private var isValueInvalid: Boolean = false
    override var currentType = Constants.TYPE.DEFAULT

    override fun setMode(mode: Int) {
        currentMode = mode
    }

    override fun setData(data: Data) {
        currentData = data
    }

    override fun getCurrentData(): Data? = currentData

    override fun resetData(type: Constants.TYPE) {
        currentData = Data(type.value, "")
        currentType = type
        _isSaveButtonEnabled.value = false
    }

    override fun checkValueValidation(result: ValidationResult) {
        when (result) {
            is ValidationResult.Valid -> {
                isValueInvalid = false
                setValue(result.value)
                _isSaveButtonEnabled.value = true
            }
            is ValidationResult.Invalid -> {
                isValueInvalid = true
                _isSaveButtonEnabled.value = false
            }
        }
    }

    override fun setAttribute(
        attr1: String?,
        attr2: String?,
        attr3: String?,
        attr4: String?,
        attr5: String?
    ) {
        attr1?.let {
            checkIfDataIsModified(currentData.attr1.orEmpty(), it, true)
            currentData.attr1 = it
        }
        attr2?.let {
            checkIfDataIsModified(currentData.attr2.orEmpty(), it)
            currentData.attr2 = it
        }
        attr3?.let {
            checkIfDataIsModified(currentData.attr3.orEmpty(), it, true)
            currentData.attr3 = it
        }
        attr4?.let {
            checkIfDataIsModified(currentData.attr4.orEmpty(), it, true)
            currentData.attr4 = it
        }
        attr5?.let {
            checkIfDataIsModified(currentData.attr5.orEmpty(), it, true)
            currentData.attr5 = it
        }
    }

    override fun saveData() {
        ioLaunch(spv) {
            when (currentMode) {
                DataInputActivity.ADD -> repo.insert(currentData)
                DataInputActivity.EDIT -> repo.update(currentData)
            }
            // post ui success
            _isOperationSuccess.postValue(true to currentMode)
        }
    }

    override fun hasUnsavedData(toggle: Boolean) {
        hasUnsavedData.value = toggle
    }

    override fun hasUnsavedData(): Boolean {
        return currentMode == DataInputActivity.ADD && hasUnsavedData.value ?: false
    }

    private fun setValue(newValue: String) {
        currentData.value = newValue
    }

    private fun checkIfDataIsModified(
        oldValue: String,
        newValue: String,
        isOptional: Boolean = false
    ) {
        if (
            currentMode == DataInputActivity.EDIT &&
            newValue != oldValue &&
            !isValueInvalid &&
            (newValue.isNotEmpty() || isOptional)
        ) {
            _isSaveButtonEnabled.value = true
        }
    }
}