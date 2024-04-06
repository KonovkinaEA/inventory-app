package com.example.inventoryapp.ui.screens.identification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val scannerManager: ScannerManager
) : ViewModel() {

    private val initialItem = InventoryItem()

    private val _uiState = MutableStateFlow(IdentificationUiState(initialItem))
    val uiState = _uiState.asStateFlow()

    private val _closeScreen = Channel<Boolean>()
    val closeScreen = _closeScreen.receiveAsFlow()

    fun onUiAction(action: IdentificationUiAction) {
        when (action) {
            IdentificationUiAction.CloseScreen -> viewModelScope.launch {
                _closeScreen.send(true)
            }
            IdentificationUiAction.SaveData -> {
                viewModelScope.launch { _closeScreen.send(true) }
                // TODO: отправка новых данных в репозиторий
            }
            IdentificationUiAction.StartScanning -> startScanning()
            is IdentificationUiAction.UpdateBarcode -> {
                updateState(uiState.value.item.copy(barcode = action.barcode))
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
                if (!it.isNullOrBlank()) updateState(uiState.value.item.copy(barcode = it))
            }
        }
    }

    private fun updateState(item: InventoryItem) {
        viewModelScope.launch(ioDispatcher) {
            val enableSave =
                item != initialItem && !(item.code.isEmpty() && item.number.isEmpty() &&
                        item.auditorium.isEmpty() && item.type.isEmpty() && item.barcode.isEmpty())
            _uiState.value = IdentificationUiState(item, enableSave)
        }
    }
}

data class IdentificationUiState(
    val item: InventoryItem = InventoryItem(),
    val enableSave: Boolean = false
)
