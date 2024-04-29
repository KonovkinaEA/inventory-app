package com.example.inventoryapp.ui.screens.identification.model

sealed class IdentificationUiAction {
    data object CloseScreen : IdentificationUiAction()
    data object SaveItem : IdentificationUiAction()
    data object DeleteItem : IdentificationUiAction()
    data object EditMode : IdentificationUiAction()
    data object StartScanning : IdentificationUiAction()
    data object SubmitBarcode : IdentificationUiAction()
    data object SubmitCode : IdentificationUiAction()
    data object SubmitInventoryNum : IdentificationUiAction()

    data class UpdateBarcode(val barcode: String) : IdentificationUiAction()
    data class UpdateCode(val code: String) : IdentificationUiAction()
    data class UpdateInventoryNumber(val inventoryNum: String) : IdentificationUiAction()
    data class UpdateName(val name: String) : IdentificationUiAction()
    data class UpdateLocation(val location: String) : IdentificationUiAction()
    data class UpdateCount(val count: Int?) : IdentificationUiAction()
    data class UpdateFactoryNum(val factoryNum: String) : IdentificationUiAction()
    data class UpdateBuilding(val building: String) : IdentificationUiAction()
}
