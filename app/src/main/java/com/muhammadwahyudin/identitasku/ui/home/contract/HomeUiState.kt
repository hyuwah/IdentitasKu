package com.muhammadwahyudin.identitasku.ui.home.contract

import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel.Companion.SORT

sealed class HomeUiState {
    object EmptyNoData : HomeUiState()
    data class EmptyFiltered(val filter: List<Constants.TYPE>) : HomeUiState()
    data class HasData(
        val datas: List<DataWithDataType>,
        val sort: SORT,
        val filter: List<Constants.TYPE>,
        val scrollToPos: Int = 0
    ) : HomeUiState()
}