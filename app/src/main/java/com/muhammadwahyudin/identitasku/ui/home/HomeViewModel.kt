package com.muhammadwahyudin.identitasku.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.ui._base.BaseViewModel
import kotlinx.coroutines.*
import timber.log.Timber

class HomeViewModel(private val appRepository: IAppRepository) : BaseViewModel() {

    private val spv = SupervisorJob()

    private val dataWithType = MutableLiveData<List<DataWithDataType>>(listOf())
    private val dataType = MutableLiveData<List<DataType>>(listOf())
    private val existingUniqueDataType = MutableLiveData<List<DataType>>(listOf())

    fun addData(data: Data) = ioLaunch(spv) {
        appRepository.insert(data)
        dataWithType.postValue(
            appRepository.getAllDataWithType()
        )
    }

    fun getAllData() = ioLaunch(spv) { appRepository.getAllData() }
    fun deleteAllData() = ioLaunch(spv) { appRepository.deleteAllData() }
    fun deleteData(data: Data) = ioLaunch(spv) { appRepository.delete(data) }
    fun deleteData(dataWithDataType: DataWithDataType) {
        ioLaunch(spv) { appRepository.deleteById(dataWithDataType.id) }
        Timber.d("Delete ${dataWithDataType.id} - ${dataWithDataType.value}")
    }

    fun deleteDatas(datasWithDataType: List<DataWithDataType>) {
        val listOfId = arrayListOf<Int>()
        for (data in datasWithDataType) {
            listOfId.add(data.id)
        }
        ioLaunch(spv) {
            appRepository.deleteDatasById(listOfId)
            dataWithType.postValue(
                appRepository.getAllDataWithType()
            )
        }
    }

    fun updateData(data: Data) {
        Timber.d("Update $data")
        ioLaunch(spv) {
            appRepository.update(data)
            dataWithType.postValue(
                appRepository.getAllDataWithType()
            )
        }
    }

    fun addDataType(dataType: DataType) = ioLaunch(spv) { appRepository.insert(dataType) }
    fun getAllDataType(): LiveData<List<DataType>> {
        ioLaunch(spv) {
            dataType.postValue(
                appRepository.getAllDataType()
            )
        }
        return dataType
    }

    fun deleteAllDataType() = ioLaunch(spv) { appRepository.deleteAllDataType() }
    fun deleteDataType(dataType: DataType) = ioLaunch(spv) { appRepository.delete(dataType) }

    override fun onCleared() {
        super.onCleared()
        spv.cancel()
    }

    // Debug
    fun debugPopulateData() {
        ioLaunch(spv) {
            appRepository.deleteAllData()
            appRepository.resetDataType()
            appRepository.prepopulateData()
            dataWithType.postValue(
                appRepository.getAllDataWithType()
            )
        }
    }

    fun getAllDataWithType(): LiveData<List<DataWithDataType>> {
        ioLaunch(spv) {
            dataWithType.postValue(
                appRepository.getAllDataWithType()
            )
        }
        return dataWithType
    }

    fun getAllExistingUniqueType(): LiveData<List<DataType>> {
        ioLaunch(spv) {
            existingUniqueDataType.postValue(
                appRepository.getAllExistingUniqueType()
            )
        }
        return existingUniqueDataType
    }
}

fun ViewModel.ioLaunch(supervisor: Job? = null, block: suspend CoroutineScope.() -> Unit) {
    val context = if (supervisor != null) Dispatchers.IO + supervisor else Dispatchers.IO
    this.viewModelScope.launch(context) {
        block.invoke(this)
    }
}