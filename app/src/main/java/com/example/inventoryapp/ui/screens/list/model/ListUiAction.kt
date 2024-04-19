package com.example.inventoryapp.ui.screens.list.model

sealed class ListUiAction {
    data class OpenItem(val id: String) : ListUiAction()
    data object CloseScreen : ListUiAction()
}
