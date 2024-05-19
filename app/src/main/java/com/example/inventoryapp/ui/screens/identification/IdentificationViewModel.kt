package com.example.inventoryapp.ui.screens.identification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.data.Repository
import com.example.inventoryapp.data.ScannerManager
import com.example.inventoryapp.data.datastore.DataStoreManager
import com.example.inventoryapp.data.model.InventoryItem
import com.example.inventoryapp.di.IoDispatcher
import com.example.inventoryapp.ui.navigation.Identification
import com.example.inventoryapp.ui.screens.identification.model.IdentificationUiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IdentificationViewModel @Inject constructor(
    private val repository: Repository,
    private val scannerManager: ScannerManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private var username = ""
    private var beforeEditItem = InventoryItem()

    private val _uiState = MutableStateFlow(IdentificationUiState(InventoryItem()))
    val uiState = _uiState.asStateFlow()

    private val _closeScreen = Channel<Boolean>()
    val closeScreen = _closeScreen.receiveAsFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            savedStateHandle.get<String>(Identification.id)?.let { id ->
                repository.getItemById(id)
            }?.let { item ->
                updateState(item)
                _uiState.value = uiState.value.copy(enableDelete = true)
            }
            dataStoreManager.userSettings.collectLatest { settings ->
                settings.username?.let { username = it }
            }
        }
    }

    fun onUiAction(action: IdentificationUiAction) {
        when (action) {
            IdentificationUiAction.CloseScreen -> closeScreen()
            IdentificationUiAction.SaveItem -> {
                viewModelScope.launch(ioDispatcher) {
                    repository.saveItem(_uiState.value.item.copy(lastUpdatedBy = username))
                }
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
                checkItemExistByBarcode(_uiState.value.item.barcode)
            }
            IdentificationUiAction.SubmitCode -> viewModelScope.launch(ioDispatcher) {
                val code = _uiState.value.item.code
                repository.getItemByCode(code)?.let {
                    updateState(it)
                    _uiState.value = uiState.value.copy(enableDelete = true)
                } ?: run {
                    _uiState.value = uiState.value.copy(enableDelete = false)
                    val item = if (_uiState.value.editMode) {
                        uiState.value.item.copy(code = code)
                    } else {
                        InventoryItem(code = code)
                    }
                    updateState(item)
                }
            }
            IdentificationUiAction.SubmitInventoryNum -> viewModelScope.launch(ioDispatcher) {
                val inventoryNum = _uiState.value.item.inventoryNum
                repository.getItemByInventoryNum(inventoryNum)?.let {
                    updateState(it)
                    _uiState.value = uiState.value.copy(enableDelete = true)
                } ?: run {
                    _uiState.value = uiState.value.copy(enableDelete = false)
                    val item = if (_uiState.value.editMode) {
                        uiState.value.item.copy(inventoryNum = inventoryNum)
                    } else {
                        InventoryItem(inventoryNum = inventoryNum)
                    }
                    updateState(item)
                }
            }
            is IdentificationUiAction.UpdateBarcode -> {
                updateState(uiState.value.item.copy(barcode = action.barcode))
            }
            is IdentificationUiAction.UpdateCode -> {
                updateState(uiState.value.item.copy(code = action.code))
            }
            is IdentificationUiAction.UpdateInventoryNumber -> {
                updateState(uiState.value.item.copy(inventoryNum = action.inventoryNum))
            }
            is IdentificationUiAction.UpdateName -> {
                updateState(uiState.value.item.copy(name = action.name))
            }
            is IdentificationUiAction.UpdateLocation -> {
                updateState(uiState.value.item.copy(location = action.location))
            }
            is IdentificationUiAction.UpdateManufactureDate -> {
                updateState(uiState.value.item.copy(manufactureDate = action.date))
            }
            is IdentificationUiAction.UpdateCount -> {
                updateState(uiState.value.item.copy(count = action.count))
            }
            is IdentificationUiAction.UpdateFactoryNum -> {
                updateState(uiState.value.item.copy(factoryNum = action.factoryNum))
            }
            is IdentificationUiAction.UpdateBuilding -> {
                updateState(uiState.value.item.copy(building = action.building))
            }
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
        editMode && item != beforeEditItem && item.name.isNotEmpty()
}

data class IdentificationUiState(
    val item: InventoryItem = InventoryItem(),
    val editMode: Boolean = false,
    val enableSave: Boolean = false,
    val enableDelete: Boolean = false
)
