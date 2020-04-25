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
    fun sortAndFilter(sort: SORT, filter: List<Int>)
    fun List<DataWithDataType>.sort(sort: SORT): List<DataWithDataType>
    fun List<DataWithDataType>.filter(filters: List<Int>): List<DataWithDataType>
}