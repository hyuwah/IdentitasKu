package com.muhammadwahyudin.identitasku.ui.home

import android.os.Handler
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.ui._base.BaseViewModel
import timber.log.Timber

class HomeViewModel(private val appRepository: IAppRepository) : BaseViewModel() {

    fun addData(data: Data) = appRepository.insert(data)
    fun getAllData() = appRepository.getAllData()
    fun deleteAllData() = appRepository.deleteAllData()
    fun deleteData(data: Data) = appRepository.delete(data)

    fun addDataType(dataType: DataType) = appRepository.insert(dataType)
    fun getAllDataType() = appRepository.getAllDataType()
    fun deleteAllDataType() = appRepository.deleteAllDataType()
    fun deleteDataType(dataType: DataType) = appRepository.delete(dataType)

    // Debug
    fun debugPopulateData() {
        appRepository.deleteAllData()
        appRepository.resetDataType()

        appRepository.insert(DataType("KTP", true, false))
        appRepository.insert(DataType("Nomor Handphone", false, false))
        appRepository.insert(DataType("Alamat", false, false))
        appRepository.insert(DataType("Nomor PLN", false, false))
        appRepository.insert(DataType("Nomor PDAM", false, false))
        appRepository.insert(DataType("Nomor NPWP", true, false))
        appRepository.insert(DataType("Nomor Rekening Bank", false, false))
        appRepository.insert(DataType("Nomor Kartu Keluarga", true, false))
        appRepository.insert(DataType("Nomor STNK", false, false))
        appRepository.insert(DataType("Nomor Kartu Kredit", false, false))
        appRepository.insert(DataType("Nomor BPJS", false, false))
        appRepository.insert(DataType("Alamat Email", false, false))

        Handler().postDelayed({
            Timber.d("Data Type size : ${this@HomeViewModel.getAllDataType().value?.size}")
            Timber.d("Data Type index 0 : ${this@HomeViewModel.getAllDataType().value?.get(0)}")
        }, 2500)

//        appRepository.insert(Data(1,"123456789"))
//        appRepository.insert(Data(2,"123456789"))
    }

}