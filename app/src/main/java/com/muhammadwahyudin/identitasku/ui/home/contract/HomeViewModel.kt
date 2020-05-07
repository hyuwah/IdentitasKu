package com.muhammadwahyudin.identitasku.ui.home.contract

import androidx.lifecycle.LiveData
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType

interface HomeViewModel {
    fun loadAllData()
    fun refreshData()
    fun addData(data: Data, typeName: String)
    fun deleteDatas(datasWithDataType: List<DataWithDataType>)
    fun updateData(data: Data, typeName: String)
    fun getAllDataType(): LiveData<List<DataType>>
    fun getAllExistingUniqueType(): LiveData<List<DataType>>
}