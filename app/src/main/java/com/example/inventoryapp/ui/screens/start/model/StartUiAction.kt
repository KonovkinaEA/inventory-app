package com.example.inventoryapp.ui.screens.start.model

sealed class StartUiAction {
    data object OpenIdentification : StartUiAction()
    data object OpenList : StartUiAction()
    data object OpenAuditoriumList : StartUiAction()
    data object ClearAuditorium : StartUiAction()

    data class UpdateAuditorium(val auditorium: String) : StartUiAction()
}
