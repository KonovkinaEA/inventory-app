package com.example.inventoryapp.ui.screens.inventory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.data.Repository
import com.example.inventoryapp.data.ScannerManager
import com.example.inventoryapp.data.model.InventoryItem
import com.example.inventoryapp.di.IoDispatcher
import com.example.inventoryapp.ui.navigation.Inventory
import com.example.inventoryapp.ui.screens.inventory.model.InventoryUiAction
import com.example.inventoryapp.ui.screens.inventory.model.InventoryUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: Repository,
    private val scannerManager: ScannerManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<InventoryUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            savedStateHandle.get<String>(Inventory.id)?.let {
                _uiState.value = InventoryUiState(location = it)
            }
        }
    }

    fun onUiAction(action: InventoryUiAction) {
        when (action) {
            InventoryUiAction.CloseScreen -> viewModelScope.launch {
                _uiEvent.send(InventoryUiEvent.CloseScreen)
            }
            InventoryUiAction.StartScanning -> startScanning()
            InventoryUiAction.SubmitBarcode -> viewModelScope.launch(ioDispatcher) {
                repository.getItemByBarcode(_uiState.value.barcode)?.let { updateState(it) }
            }
            InventoryUiAction.SubmitCode -> viewModelScope.launch(ioDispatcher) {
                repository.getItemByCode(_uiState.value.code)?.let { updateState(it) }
            }
            InventoryUiAction.SubmitInventoryNum -> viewModelScope.launch(ioDispatcher) {
                repository.getItemByInventoryNum(_uiState.value.inventoryNum)?.let {
                    updateState(it)
                }
            }
            is InventoryUiAction.UpdateBarcode -> viewModelScope.launch {
                _uiState.value = uiState.value.copy(barcode = action.barcode)
            }
            is InventoryUiAction.UpdateCode -> viewModelScope.launch {
                _uiState.value = uiState.value.copy(code = action.code)
            }
            is InventoryUiAction.UpdateInventoryNumber -> viewModelScope.launch {
                _uiState.value = uiState.value.copy(inventoryNum = action.inventoryNum)
            }
            is InventoryUiAction.OpenItem -> viewModelScope.launch {
                _uiEvent.send(InventoryUiEvent.OpenItem(action.id))
            }
        }
    }

    fun reloadData() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = uiState.value.copy(
                list = uiState.value.list.mapNotNull { repository.getItemById(it.id) }
            )
        }
    }

    private fun startScanning() {
        viewModelScope.launch(ioDispatcher) {
            scannerManager.scanningData.collect { barcode ->
                if (!barcode.isNullOrBlank()) repository.getItemByBarcode(barcode)?.let {
                    updateState(it)
                }
            }
        }
    }

    private fun updateState(item: InventoryItem) {
        val list = _uiState.value.list.toMutableList()
        list.add(item)
        _uiState.value = uiState.value.copy(list = list)
    }
}

data class InventoryUiState(
    val location: String = "",
    val barcode: String = "",
    val code: String = "",
    val inventoryNum: String = "",
    val list: List<InventoryItem> = listOf()
)
