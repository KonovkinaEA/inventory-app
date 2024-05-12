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
        }
    }

    private fun startScanning() {
        viewModelScope.launch(ioDispatcher) {
            scannerManager.scanningData.collect {
                if (!it.isNullOrBlank()) checkItemExistByBarcode(it)
            }
        }
    }

    private suspend fun checkItemExistByBarcode(barcode: String) {
        repository.getItemByBarcode(barcode)?.let {
            val list = _uiState.value.list.toMutableList()
            list.add(it)
            _uiState.value = uiState.value.copy(list = list)
        }
    }
}

data class InventoryUiState(
    val location: String = "",
    val list: List<InventoryItem> = listOf()
)
