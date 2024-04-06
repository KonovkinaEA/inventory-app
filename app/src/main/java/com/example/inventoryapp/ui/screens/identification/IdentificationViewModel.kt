package com.example.inventoryapp.ui.screens.identification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.data.ScannerManager
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

    private val _uiState = MutableStateFlow(IdentificationUiState())
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
                // TODO: viewModelScope.launch(ioDispatcher) {}
            }
            IdentificationUiAction.StartScanning -> startScanning()
            is IdentificationUiAction.UpdateBarcode -> viewModelScope.launch {
                _uiState.value = uiState.value.copy(barcode = action.barcode)
            }
            is IdentificationUiAction.UpdateCode -> viewModelScope.launch {
                _uiState.value = uiState.value.copy(code = action.code)
            }
            is IdentificationUiAction.UpdateInventoryNumber -> viewModelScope.launch {
                _uiState.value = uiState.value.copy(number = action.number)
            }
            is IdentificationUiAction.UpdateAuditorium -> viewModelScope.launch {
                _uiState.value = uiState.value.copy(auditorium = action.auditorium)
            }
            is IdentificationUiAction.UpdateType -> viewModelScope.launch {
                _uiState.value = uiState.value.copy(type = action.type)
            }
        }
    }

    private fun startScanning() {
        viewModelScope.launch(ioDispatcher) {
            scannerManager.scanningData.collect {
                if (!it.isNullOrBlank()) {
                    _uiState.value = uiState.value.copy(barcode = it)
                }
            }
        }
    }
}

data class IdentificationUiState(
    val barcode: String = "",
    val code: String = "",
    val number: String = "",
    val auditorium: String = "",
    val type: String = ""
)
