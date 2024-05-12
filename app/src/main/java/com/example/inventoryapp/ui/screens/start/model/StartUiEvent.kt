package com.example.inventoryapp.ui.screens.start.model

sealed class StartUiEvent {
    data object OpenIdentification : StartUiEvent()
    data class OpenList(val location: String) : StartUiEvent()
    data class OpenInventory(val location: String) : StartUiEvent()
}
