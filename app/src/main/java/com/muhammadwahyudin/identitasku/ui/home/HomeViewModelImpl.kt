package com.muhammadwahyudin.identitasku.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.ui._base.BaseViewModel
import com.muhammadwahyudin.identitasku.ui.home.contract.HomeUiState
import com.muhammadwahyudin.identitasku.ui.home.contract.HomeViewModel
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel.Companion.DEFAULT_SORT
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel.Companion.SORT
import com.muhammadwahyudin.identitasku.utils.ioLaunch

class HomeViewModelImpl(private val appRepository: IAppRepository) : BaseViewModel(),
    HomeViewModel,
    SortFilterViewModel {

    private val dataWithType = MutableLiveData<List<DataWithDataType>>(listOf())
    private val dataType = MutableLiveData<List<DataType>>(listOf())
    private val existingUniqueDataType = MutableLiveData<List<DataType>>(listOf())

    private val _uiState = dataWithType.map { it.mapToUiState(0) } as MutableLiveData
    val uiState get() = _uiState as LiveData<HomeUiState>

    override var currentSort = DEFAULT_SORT
        private set
    override var currentFilter: List<Int> = listOf()
        private set

    override fun addData(data: Data, typeName: String) {
        ioLaunch(spv) {
            val newDataId = appRepository.insert(data).toInt()
            val message = "$typeName successfully added"
            postSnackbar(message)
            val list = appRepository.getAllDataWithType()
            val scrollPos = list.indexOfFirst { it.id == newDataId }
            _uiState.postValue(list.mapToUiState(if (scrollPos == -1) 0 else scrollPos))
        }
    }

    override fun deleteDatas(datasWithDataType: List<DataWithDataType>) {
        ioLaunch(spv) {
            appRepository.deleteDatasById(datasWithDataType.map { it.id })
            loadAllData()
        }
    }

    override fun updateData(data: Data, typeName: String) {
        ioLaunch(spv) {
            appRepository.update(data)
            val message = "$typeName successfully updated"
            postSnackbar(message)
            val list = appRepository.getAllDataWithType()
            val scrollPos = list.indexOfFirst { it.id == data.id }
            _uiState.postValue(list.mapToUiState(if (scrollPos == -1) 0 else scrollPos))
        }
    }

    override fun getAllDataType(): LiveData<List<DataType>> {
        ioLaunch(spv) {
            dataType.postValue(
                appRepository.getAllDataType()
            )
        }
        return dataType
    }

    override fun loadAllData() {
        ioLaunch(spv) {
            dataWithType.postValue(appRepository.getAllDataWithType())
        }
    }

    override fun getAllExistingUniqueType(): LiveData<List<DataType>> {
        ioLaunch(spv) {
            existingUniqueDataType.postValue(
                appRepository.getAllExistingUniqueType()
            )
        }
        return existingUniqueDataType
    }

    override fun List<DataWithDataType>.sort(sort: SORT): List<DataWithDataType> {
        return when (sort) {
            SORT.NEWEST -> this
            SORT.OLDEST -> this.asReversed()
            SORT.CATEGORY -> this.sortedBy { it.typeId }
        }
    }

    override fun List<DataWithDataType>.filter(filters: List<Int>): List<DataWithDataType> {
        return if (filters.isNotEmpty()) {
            this.filter { filters.contains(it.typeId) }
        } else {
            this
        }
    }

    override fun sortAndFilter(sort: SORT, filter: List<Int>) {
        currentSort = sort
        currentFilter = filter
        loadAllData()
    }

    private fun List<DataWithDataType>.mapToUiState(scrollPos: Int): HomeUiState {
        return if (this.isEmpty()) {
            HomeUiState.EmptyNoData
        } else {
            val filters = currentFilter.map { typeId ->
                Constants.TYPE.values().find { it.value == typeId } ?: Constants.TYPE.DEFAULT
            }
            val uiList = this.sort(currentSort).filter(currentFilter)
            if (uiList.isEmpty())
                HomeUiState.EmptyFiltered(filters)
            else
                HomeUiState.HasData(uiList, currentSort, filters, scrollPos)
        }
    }

    // Debug
    fun debugPopulateData() {
        ioLaunch(spv) {
            appRepository.deleteAllData()
            appRepository.resetDataType()
            appRepository.prepopulateData()
            loadAllData()
        }
    }
}