package com.example.inventoryapp.ui.screens.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.data.datastore.DataStoreManager
import com.example.inventoryapp.di.IoDispatcher
import com.example.inventoryapp.ui.screens.start.model.StartUiAction
import com.example.inventoryapp.ui.screens.start.model.StartUiEvent
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
class StartViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(StartUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<StartUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            dataStoreManager.userSettings.collectLatest { settings ->
                settings.ipAddress?.let { _uiState.value = StartUiState(ipAddress = it) }
            }
        }
    }

    fun onUiAction(action: StartUiAction) = viewModelScope.launch {
        when (action) {
            StartUiAction.OpenIdentification -> _uiEvent.send(StartUiEvent.OpenIdentification)
            StartUiAction.OpenList -> _uiEvent.send(StartUiEvent.OpenList(location = ""))
            StartUiAction.OpenInventory ->
                _uiEvent.send(StartUiEvent.OpenInventory(_uiState.value.location))
            StartUiAction.OpenLocationList ->
                _uiEvent.send(StartUiEvent.OpenList(_uiState.value.location))
            StartUiAction.ClearLocation ->
                _uiState.value = uiState.value.copy(location = "")
            StartUiAction.SaveIpAddress -> viewModelScope.launch(ioDispatcher) {
                dataStoreManager.saveIpAddress(_uiState.value.ipAddress)
            }
            is StartUiAction.UpdateLocation ->
                _uiState.value = uiState.value.copy(location = action.location)
            is StartUiAction.UpdateIpAddress ->
                _uiState.value = uiState.value.copy(ipAddress = action.address)
        }
    }
}

data class StartUiState(
    val location: String = "",
    val ipAddress: String = ""
)
