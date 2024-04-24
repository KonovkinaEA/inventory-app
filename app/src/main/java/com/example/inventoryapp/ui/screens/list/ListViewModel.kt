package com.example.inventoryapp.ui.screens.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.data.Repository
import com.example.inventoryapp.data.model.InventoryItem
import com.example.inventoryapp.di.IoDispatcher
import com.example.inventoryapp.ui.navigation.ItemsList
import com.example.inventoryapp.ui.screens.list.model.ListUiAction
import com.example.inventoryapp.ui.screens.list.model.ListUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<ListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(ioDispatcher) {
            val list = savedStateHandle.get<String>(ItemsList.id)?.let {
                _uiState.value = ListUiState(auditorium = it)
                repository.getItemsInAuditorium(it)
            } ?: repository.getAllItems()
            _uiState.value = uiState.value.copy(list = list)
        }
    }

    fun onUiAction(action: ListUiAction) {
        when (action) {
            is ListUiAction.OpenItem -> viewModelScope.launch {
                _uiEvent.send(ListUiEvent.OpenItem(action.id))
            }
            ListUiAction.CloseScreen -> viewModelScope.launch {
                _uiEvent.send(ListUiEvent.CloseScreen)
            }
        }
    }
}

data class ListUiState(
    val auditorium: String = "",
    val list: List<InventoryItem> = emptyList()
)
