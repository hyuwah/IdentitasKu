package com.muhammadwahyudin.identitasku.ui.datainput.contract

import androidx.lifecycle.LiveData
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.Data

interface DataInputViewModel {
    val isSaveButtonEnabled: LiveData<Boolean>
    val isOperationSuccess: LiveData<Pair<Boolean, Int>>
    val currentType: Constants.TYPE
    fun hasUnsavedData(): Boolean
    fun setMode(mode: Int)
    fun setData(data: Data)
    fun resetData(type: Constants.TYPE)
    fun saveData()
}