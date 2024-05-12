package com.example.inventoryapp.ui.screens.start.model

sealed class StartUiAction {
    data object OpenIdentification : StartUiAction()
    data object OpenList : StartUiAction()
    data object OpenInventory : StartUiAction()
    data object OpenLocationList : StartUiAction()
    data object ClearLocation : StartUiAction()

    data class UpdateLocation(val location: String) : StartUiAction()
}
