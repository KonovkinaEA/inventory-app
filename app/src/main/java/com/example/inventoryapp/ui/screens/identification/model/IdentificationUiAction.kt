package com.example.inventoryapp.ui.screens.identification.model

sealed class IdentificationUiAction {
    data object CloseScreen : IdentificationUiAction()
    data object SaveData : IdentificationUiAction()
    data object StartScanning : IdentificationUiAction()

    data class UpdateBarcode(val barcode: String) : IdentificationUiAction()
    data class UpdateCode(val code: String) : IdentificationUiAction()
    data class UpdateInventoryNumber(val number: String) : IdentificationUiAction()
    data class UpdateAuditorium(val auditorium: String) : IdentificationUiAction()
    data class UpdateType(val type: String) : IdentificationUiAction()
}
