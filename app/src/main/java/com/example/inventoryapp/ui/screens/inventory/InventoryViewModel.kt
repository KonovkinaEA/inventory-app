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

    private lateinit var openedItemId: String

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
            InventoryUiAction.EndProcess -> viewModelScope.launch(ioDispatcher) {
                val currentList = _uiState.value.list.toMutableList()
                repository.getItemsByLocation(_uiState.value.location).forEach { item ->
                    if (!_uiState.value.list.any { item.id == it.id }) currentList.add(
                        item.copy(isCorrectlyPlaced = false)
                    )
                }
                _uiState.value = uiState.value.copy(list = currentList.toList(), endProcess = true)
                currentList.forEach {
                    repository.saveItem(it)
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
                openedItemId = action.id
                _uiEvent.send(InventoryUiEvent.OpenItem(action.id))
            }
        }
    }

    fun reloadData() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value.list.find { it.id == openedItemId }?.let { item ->
                repository.getItemById(openedItemId)?.let { newItem ->
                    var updatedItem = item

                    if (item.name != newItem.name) updatedItem = newItem
                    if (item.location != newItem.location) {
                        updatedItem = newItem.copy(
                            isCorrectlyPlaced = newItem.location == uiState.value.location
                        )
                    }
                    if (updatedItem != item) {
                        _uiState.value = uiState.value.copy(
                            list = uiState.value.list.map {
                                if (it.id == openedItemId) updatedItem else it
                            }
                        )
                    }
                }
            }
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
        val updatedItem = item.copy(isCorrectlyPlaced = item.location == _uiState.value.location)
        val list = _uiState.value.list.toMutableList()
        list.add(updatedItem)
        _uiState.value = uiState.value.copy(list = list)
    }
}

data class InventoryUiState(
    val location: String = "",
    val barcode: String = "",
    val code: String = "",
    val inventoryNum: String = "",
    val list: List<InventoryItem> = listOf(),
    val endProcess: Boolean = false
)
