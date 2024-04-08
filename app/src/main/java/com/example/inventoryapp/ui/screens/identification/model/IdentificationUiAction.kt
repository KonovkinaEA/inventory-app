package com.example.inventoryapp.ui.screens.identification.model

import com.example.inventoryapp.data.model.ItemType

sealed class IdentificationUiAction {
    data object CloseScreen : IdentificationUiAction()
    data object SaveItem : IdentificationUiAction()
    data object DeleteItem : IdentificationUiAction()
    data object EditMode : IdentificationUiAction()
    data object StartScanning : IdentificationUiAction()
    data object SubmitBarcode : IdentificationUiAction()

    data class UpdateBarcode(val barcode: String) : IdentificationUiAction()
    data class UpdateName(val name: String) : IdentificationUiAction()
    data class UpdateCode(val code: String) : IdentificationUiAction()
    data class UpdateInventoryNumber(val number: String) : IdentificationUiAction()
    data class UpdateAuditorium(val auditorium: String) : IdentificationUiAction()
    data class UpdateType(val type: ItemType) : IdentificationUiAction()
}
