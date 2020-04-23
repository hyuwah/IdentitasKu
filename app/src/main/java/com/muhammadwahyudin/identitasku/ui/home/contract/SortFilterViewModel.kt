package com.muhammadwahyudin.identitasku.ui.home.contract

import com.muhammadwahyudin.identitasku.data.model.DataWithDataType

interface SortFilterViewModel {
    companion object {
        enum class SORT {
            NEWEST,
            OLDEST,
            CATEGORY,
        }

        val DEFAULT_SORT = SORT.NEWEST
    }

    val currentSort: SORT
    val currentFilter: List<Int>
    fun sortData(datas: List<DataWithDataType>, sort: SORT): List<DataWithDataType>
    fun filterData(datas: List<DataWithDataType>, filter: List<Int>): List<DataWithDataType>
    fun sortAndFilter(sort: SORT, filter: List<Int>)
}