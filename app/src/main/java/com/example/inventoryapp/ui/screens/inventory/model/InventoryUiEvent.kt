package com.example.inventoryapp.ui.screens.inventory.model

sealed class InventoryUiEvent {
    data object CloseScreen : InventoryUiEvent()
    data class OpenItem(val id: String) : InventoryUiEvent()
}
