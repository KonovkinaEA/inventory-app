package com.example.inventoryapp.ui.screens.list.model

sealed class ListUiEvent {
    data class OpenItem(val id: String) : ListUiEvent()
    data object CloseScreen : ListUiEvent()
}
