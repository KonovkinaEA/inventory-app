package com.example.inventoryapp.ui.screens.identification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.data.Repository
import com.example.inventoryapp.data.ScannerManager
import com.example.inventoryapp.data.model.InventoryItem
import com.example.inventoryapp.di.IoDispatcher
import com.example.inventoryapp.ui.screens.identification.model.IdentificationUiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IdentificationViewModel @Inject constructor(
    private val repository: Repository,
    private val scannerManager: ScannerManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val initialItem = InventoryItem()
    private var beforeEditItem = InventoryItem()

    private val _uiState = MutableStateFlow(IdentificationUiState(initialItem))
    val uiState = _uiState.asStateFlow()

    private val _closeScreen = Channel<Boolean>()
    val closeScreen = _closeScreen.receiveAsFlow()

    fun onUiAction(action: IdentificationUiAction) {
        when (action) {
            IdentificationUiAction.CloseScreen -> closeScreen()
            IdentificationUiAction.SaveItem -> {
                viewModelScope.launch(ioDispatcher) { repository.saveItem(_uiState.value.item) }
                closeScreen()
            }
            IdentificationUiAction.DeleteItem -> {
                viewModelScope.launch(ioDispatcher) { repository.deleteItem(_uiState.value.item.id) }
                closeScreen()
            }
            IdentificationUiAction.EditMode -> viewModelScope.launch {
                val editMode = uiState.value.editMode
                if (!editMode) beforeEditItem = uiState.value.item
                _uiState.value = uiState.value.copy(
                    item = beforeEditItem,
                    editMode = !editMode,
                    enableSave = enableSave(uiState.value.item, editMode = true)
                )
            }
            IdentificationUiAction.StartScanning -> startScanning()
            IdentificationUiAction.SubmitBarcode -> viewModelScope.launch(ioDispatcher) {
                checkItemExist(_uiState.value.item.barcode)
            }
            is IdentificationUiAction.UpdateBarcode -> {
                updateState(uiState.value.item.copy(barcode = action.barcode))
            }
            is IdentificationUiAction.UpdateName -> {
                updateState(uiState.value.item.copy(name = action.name))
            }
            is IdentificationUiAction.UpdateCode -> {
                updateState(uiState.value.item.copy(code = action.code))
            }
            is IdentificationUiAction.UpdateInventoryNumber -> {
                updateState(uiState.value.item.copy(number = action.number))
            }
            is IdentificationUiAction.UpdateAuditorium -> {
                updateState(uiState.value.item.copy(auditorium = action.auditorium))
            }
            is IdentificationUiAction.UpdateType -> {
                updateState(uiState.value.item.copy(type = action.type))
            }
        }
    }

    private fun startScanning() {
        viewModelScope.launch(ioDispatcher) {
            scannerManager.scanningData.collect {
                if (!it.isNullOrBlank()) checkItemExist(it)
            }
        }
    }

    private suspend fun checkItemExist(barcode: String) {
        repository.getItemByBarcode(barcode)?.let {
            updateState(it)
            _uiState.value = uiState.value.copy(enableDelete = true)
        } ?: run {
            _uiState.value = uiState.value.copy(enableDelete = false)
            val item = if (_uiState.value.editMode) {
                uiState.value.item.copy(barcode = barcode)
            } else {
                InventoryItem(barcode = barcode)
            }
            updateState(item)
        }
    }

    private fun updateState(item: InventoryItem) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(
                item = item,
                enableSave = enableSave(item, uiState.value.editMode)
            )
        }
    }

    private fun closeScreen() {
        viewModelScope.launch { _closeScreen.send(true) }
    }

    private fun enableSave(item: InventoryItem, editMode: Boolean) =
        editMode && item != initialItem && item.barcode.isNotEmpty() && item.name.isNotEmpty()
}

data class IdentificationUiState(
    val item: InventoryItem = InventoryItem(),
    val editMode: Boolean = false,
    val enableSave: Boolean = false,
    val enableDelete: Boolean = false
)
