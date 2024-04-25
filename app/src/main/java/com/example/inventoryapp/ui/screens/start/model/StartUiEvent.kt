package com.example.inventoryapp.ui.screens.start.model

sealed class StartUiEvent {
    data object OpenIdentification : StartUiEvent()
    data class OpenList(val auditorium: String) : StartUiEvent()
}
