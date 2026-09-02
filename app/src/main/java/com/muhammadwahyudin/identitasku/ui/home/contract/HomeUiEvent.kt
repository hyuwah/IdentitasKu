package com.muhammadwahyudin.identitasku.ui.home.contract

import com.muhammadwahyudin.identitasku.data.model.DataWithDataType

sealed interface HomeUiEvent {
    data object AddNewItem: HomeUiEvent
    data object OpenSettingsPage: HomeUiEvent
    data object OpenFilterPage: HomeUiEvent
    sealed interface OnItemAction: HomeUiEvent {
        data class EditItem(val data: DataWithDataType): OnItemAction
        data class ShareItem(val data: DataWithDataType): OnItemAction
        data class CopyValueToClipboard(val value: String, val typeName: String): OnItemAction
        data class DeleteItem(val pos: Int, val data: DataWithDataType): OnItemAction
    }
}