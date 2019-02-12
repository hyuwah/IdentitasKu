package com.muhammadwahyudin.identitasku.ui.home

import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.ui._base.BaseViewModel

class HomeViewModel(private val appRepository: IAppRepository) : BaseViewModel() {

    fun addData(data: Data) = appRepository.insert(data)
    fun getAllData() = appRepository.getAllData()
    fun deleteData(data: Data) = appRepository.delete(data)

    fun addDataType(dataType: DataType) = appRepository.insert(dataType)
    fun getAllDataType() = appRepository.getAllDataType()
    fun deleteDataType(dataType: DataType) = appRepository.delete(dataType)

}