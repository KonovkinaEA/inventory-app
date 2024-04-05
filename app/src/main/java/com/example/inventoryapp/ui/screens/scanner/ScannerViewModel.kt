package com.example.inventoryapp.ui.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.data.ScannerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scannerManager: ScannerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()

    fun startScanning() {
        viewModelScope.launch {
            scannerManager.scanningData.collect {
                if (!it.isNullOrBlank()) {
                    _uiState.value = uiState.value.copy(details = it)
                }
            }
        }
    }
}

data class ScannerUiState(
    val details: String = "Start scanning to get details"
)