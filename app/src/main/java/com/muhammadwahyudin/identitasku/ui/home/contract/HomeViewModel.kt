package com.muhammadwahyudin.identitasku.ui.home.contract

import androidx.lifecycle.LiveData
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType

interface HomeViewModel {
    fun addData(data: Data)
    fun deleteDatas(datasWithDataType: List<DataWithDataType>)
    fun updateData(data: Data)
    fun getAllDataType(): LiveData<List<DataType>>
    fun getAllDataWithType(): LiveData<List<DataWithDataType>>
    fun getAllExistingUniqueType(): LiveData<List<DataType>>
}