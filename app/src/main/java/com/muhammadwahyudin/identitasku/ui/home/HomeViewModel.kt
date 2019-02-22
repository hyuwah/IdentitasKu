package com.muhammadwahyudin.identitasku.ui.home

import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.ui._base.BaseViewModel

class HomeViewModel(private val appRepository: IAppRepository) : BaseViewModel() {

    fun addData(data: Data) = appRepository.insert(data)
    fun getAllData() = appRepository.getAllData()
    fun deleteAllData() = appRepository.deleteAllData()
    fun deleteData(data: Data) = appRepository.delete(data)
    fun deleteData(dataWithDataType: DataWithDataType) {
//        var data = Data(
//            dataWithDataType.typeId,
//            dataWithDataType.value,
//            dataWithDataType.attr1,
//            dataWithDataType.attr2,
//            dataWithDataType.attr3,
//            dataWithDataType.attr4,
//            dataWithDataType.attr5
//            )
//        data.id = dataWithDataType.id
//        Timber.d("data = $data")
        appRepository.deleteById(dataWithDataType.id)
    }

    fun addDataType(dataType: DataType) = appRepository.insert(dataType)
    fun getAllDataType() = appRepository.getAllDataType()
    fun deleteAllDataType() = appRepository.deleteAllDataType()
    fun deleteDataType(dataType: DataType) = appRepository.delete(dataType)

    // Debug
    fun debugPopulateData() {
        appRepository.deleteAllData()
        appRepository.resetDataType()
        appRepository.prepopulateData()
    }

    fun getAllDataWithType() = appRepository.getAllDataWithType()
    fun getAllExistingUniqueType() = appRepository.getAllExistingUniqueType()
}