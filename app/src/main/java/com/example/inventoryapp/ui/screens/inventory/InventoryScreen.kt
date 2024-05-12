package com.example.inventoryapp.ui.screens.inventory

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inventoryapp.ui.common.MenuElevatedCard
import com.example.inventoryapp.ui.screens.inventory.components.InventoryBottomAppBar
import com.example.inventoryapp.ui.screens.inventory.components.InventoryTopAppBar
import com.example.inventoryapp.ui.screens.inventory.model.InventoryUiAction
import com.example.inventoryapp.ui.screens.inventory.model.InventoryUiEvent
import com.example.inventoryapp.ui.screens.list.components.MenuTitle
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview
import com.example.inventoryapp.ui.theme.White

@Composable
fun InventoryScreen(closeScreen: () -> Unit, viewModel: InventoryViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                InventoryUiEvent.CloseScreen -> closeScreen()
            }
        }
    }

    InventoryScreenContent(state, viewModel::onUiAction)
}

@Composable
private fun InventoryScreenContent(
    state: InventoryUiState,
    onUiAction: (InventoryUiAction) -> Unit
) {
    Scaffold(
        topBar = { InventoryTopAppBar(location = state.location, onUiAction) },
        bottomBar = { InventoryBottomAppBar { onUiAction(InventoryUiAction.StartScanning) } },
        containerColor = ExtendedTheme.colors.backPrimary
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }
            items(state.list) {
                if (it.name.isNotEmpty()) {
                    val cardColors = if (state.location == it.location) {
                        ExtendedTheme.correctCardColors
                    } else {
                        ExtendedTheme.incorrectCardColors
                    }
                    MenuElevatedCard(cardColors = cardColors, onClick = {}) {
                        MenuTitle(text = it.name, textColor = White)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun InventoryScreenPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        InventoryScreenContent(InventoryUiState(location = "3-403")) {}
    }
}
