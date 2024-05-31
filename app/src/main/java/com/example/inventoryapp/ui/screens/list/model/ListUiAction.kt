package com.example.inventoryapp.ui.screens.list.model

sealed class ListUiAction {
    data object CloseScreen : ListUiAction()
    data object DownloadExcel : ListUiAction()

    data class OpenItem(val id: String) : ListUiAction()
}
