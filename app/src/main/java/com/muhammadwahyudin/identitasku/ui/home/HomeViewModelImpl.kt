package com.muhammadwahyudin.identitasku.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository
import com.muhammadwahyudin.identitasku.ui._base.BaseViewModel
import com.muhammadwahyudin.identitasku.ui.home.contract.HomeViewModel
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel.Companion.DEFAULT_SORT
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel.Companion.SORT
import com.muhammadwahyudin.identitasku.utils.ioLaunch
import timber.log.Timber

// TODO implement state ui instead langsung list data (Empty Data, Has Data, Filtered, etc)
class HomeViewModelImpl(private val appRepository: IAppRepository) : BaseViewModel(),
    HomeViewModel,
    SortFilterViewModel {

    private val dataWithType = MutableLiveData<List<DataWithDataType>>(listOf())
    private val dataType = MutableLiveData<List<DataType>>(listOf())
    private val existingUniqueDataType = MutableLiveData<List<DataType>>(listOf())

    override var currentSort = DEFAULT_SORT
        private set
    override var currentFilter: List<Int> = listOf()
        private set

    override fun addData(data: Data) = ioLaunch(spv) {
        appRepository.insert(data)
        // TODO should scroll to position of newly created data instead of to top
        // TODO should respect filter, informs user of active filter (might not shows/filtered)
        dataWithType.postValue(
            sortData(appRepository.getAllDataWithType(), currentSort)
        )
    }

    override fun deleteDatas(datasWithDataType: List<DataWithDataType>) {
        ioLaunch(spv) {
            appRepository.deleteDatasById(datasWithDataType.map { it.id })
            dataWithType.postValue(
                sortData(appRepository.getAllDataWithType(), currentSort)
            )
        }
    }

    override fun updateData(data: Data) {
        Timber.d("Update $data")
        ioLaunch(spv) {
            appRepository.update(data)
            sortAndFilter(currentSort, currentFilter)
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

    override fun getAllDataWithType(): LiveData<List<DataWithDataType>> {
        ioLaunch(spv) {
            dataWithType.postValue(
                sortData(appRepository.getAllDataWithType(), currentSort)
            )
        }
        return dataWithType
    }

    override fun getAllExistingUniqueType(): LiveData<List<DataType>> {
        ioLaunch(spv) {
            existingUniqueDataType.postValue(
                appRepository.getAllExistingUniqueType()
            )
        }
        return existingUniqueDataType
    }

    override fun sortData(datas: List<DataWithDataType>, sort: SORT): List<DataWithDataType> {
        return when (sort) {
            SORT.NEWEST -> datas
            SORT.OLDEST -> datas.asReversed()
            SORT.CATEGORY -> datas.sortedBy { it.typeId }
        }
    }

    override fun filterData(
        datas: List<DataWithDataType>,
        filter: List<Int>
    ): List<DataWithDataType> {
        return if (filter.isNotEmpty()) {
            datas.filter { filter.contains(it.typeId) }
        } else {
            datas
        }
    }

    override fun sortAndFilter(sort: SORT, filter: List<Int>) {
        // Original full list keep di viewmodel
        // ... disini bisa ada manipulasi / proses
        // Expose display list ke view
        currentSort = sort
        currentFilter = filter
        ioLaunch(spv) {
            var datas = appRepository.getAllDataWithType()
            datas = sortData(datas, currentSort)
            datas = filterData(datas, currentFilter)

            // TODO mesti ubah ke *state* livedata biar gampang
            dataWithType.postValue(datas)
        }
    }

    // Debug
    fun debugPopulateData() {
        ioLaunch(spv) {
            appRepository.deleteAllData()
            appRepository.resetDataType()
            appRepository.prepopulateData()
            dataWithType.postValue(
                sortData(appRepository.getAllDataWithType(), currentSort)
            )
        }
    }
}