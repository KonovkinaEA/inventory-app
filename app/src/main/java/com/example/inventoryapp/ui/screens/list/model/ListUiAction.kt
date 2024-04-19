package com.example.inventoryapp.ui.screens.list.model

sealed class ListUiAction {
    data object CloseScreen : ListUiAction()
}
