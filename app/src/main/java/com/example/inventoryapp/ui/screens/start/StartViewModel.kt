package com.example.inventoryapp.ui.screens.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.ui.screens.start.model.StartUiAction
import com.example.inventoryapp.ui.screens.start.model.StartUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(StartUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<StartUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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
            StartUiAction.SaveUsername -> { /*TODO*/
            }
            is StartUiAction.UpdateLocation ->
                _uiState.value = uiState.value.copy(location = action.location)
            is StartUiAction.UpdateUsername ->
                _uiState.value = uiState.value.copy(username = action.name)
        }
    }
}

data class StartUiState(
    val location: String = "",
    val username: String = ""
)
