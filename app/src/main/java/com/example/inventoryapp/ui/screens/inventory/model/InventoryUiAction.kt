package com.example.inventoryapp.ui.screens.inventory.model

sealed class InventoryUiAction {
    data object CloseScreen : InventoryUiAction()
    data object StartScanning : InventoryUiAction()
}
