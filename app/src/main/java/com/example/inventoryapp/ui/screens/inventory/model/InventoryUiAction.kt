package com.example.inventoryapp.ui.screens.inventory.model

sealed class InventoryUiAction {
    data object CloseScreen : InventoryUiAction()
    data object StartScanning : InventoryUiAction()
    data object SubmitBarcode : InventoryUiAction()
    data object SubmitCode : InventoryUiAction()
    data object SubmitInventoryNum : InventoryUiAction()
    data object EndProcess : InventoryUiAction()
    data object GetReport : InventoryUiAction()

    data class UpdateBarcode(val barcode: String) : InventoryUiAction()
    data class UpdateCode(val code: String) : InventoryUiAction()
    data class UpdateInventoryNumber(val inventoryNum: String) : InventoryUiAction()
    data class OpenItem(val id: String) : InventoryUiAction()
}
