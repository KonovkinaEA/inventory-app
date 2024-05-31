package com.example.inventoryapp.ui.screens.inventory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inventoryapp.ui.common.MenuCardButton
import com.example.inventoryapp.ui.common.MenuElevatedCard
import com.example.inventoryapp.ui.common.MenuInputField
import com.example.inventoryapp.ui.screens.inventory.components.InventoryTopAppBar
import com.example.inventoryapp.ui.screens.inventory.model.InventoryUiAction
import com.example.inventoryapp.ui.screens.inventory.model.InventoryUiEvent
import com.example.inventoryapp.ui.screens.list.components.MenuTitle
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview
import com.example.inventoryapp.ui.theme.White

@Composable
fun InventoryScreen(
    reload: Boolean,
    closeScreen: () -> Unit,
    openItem: (String) -> Unit,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    if (reload) viewModel.reloadData()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                InventoryUiEvent.CloseScreen -> closeScreen()
                is InventoryUiEvent.OpenItem -> openItem(it.id)
            }
        }
    }

    InventoryScreenContent(state, viewModel::onUiAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InventoryScreenContent(
    state: InventoryUiState,
    onUiAction: (InventoryUiAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val localDensity = LocalDensity.current
    var columnHeightDp by remember { mutableStateOf(0.dp) }

    BottomSheetScaffold(
        topBar = { InventoryTopAppBar(state, onUiAction) },
        sheetContent = {

            Column(
                modifier = Modifier
                    .onGloballyPositioned {
                        columnHeightDp = with(localDensity) { it.size.height.toDp() }
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                if (!state.endProcess) {
                    MenuCardButton(text = "Сканировать штрихкод") {
                        onUiAction(InventoryUiAction.StartScanning)
                    }
                } else {
                    MenuCardButton(text = "Получить отчет") {
                        onUiAction(InventoryUiAction.GetReport)
                    }
                }
            }
            Column(modifier = Modifier.padding(20.dp)) {
                if (!state.endProcess) {
                    MenuInputField(
                        label = "Штрихкод",
                        value = state.barcode,
                        readOnly = false,
                        confirmButton = true,
                        confirm = {
                            onUiAction(InventoryUiAction.SubmitBarcode)
                            focusManager.clearFocus()
                        }
                    ) { onUiAction(InventoryUiAction.UpdateBarcode(it)) }
                    MenuInputField(
                        label = "Код",
                        value = state.code,
                        readOnly = false,
                        confirmButton = true,
                        confirm = {
                            onUiAction(InventoryUiAction.SubmitCode)
                            focusManager.clearFocus()
                        }
                    ) { onUiAction(InventoryUiAction.UpdateCode(it)) }
                    MenuInputField(
                        label = "Инвентарный номер",
                        value = state.inventoryNum,
                        readOnly = false,
                        confirmButton = true,
                        confirm = {
                            onUiAction(InventoryUiAction.SubmitInventoryNum)
                            focusManager.clearFocus()
                        }
                    ) { onUiAction(InventoryUiAction.UpdateInventoryNumber(it)) }
                }
            }
        },
        sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight + columnHeightDp,
        sheetShadowElevation = 10.dp,
        sheetSwipeEnabled = !state.endProcess,
        sheetContainerColor = ExtendedTheme.colors.backSecondary,
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
                    val cardColors = if (it.isCorrectlyPlaced) {
                        ExtendedTheme.correctCardColors
                    } else {
                        ExtendedTheme.incorrectCardColors
                    }
                    MenuElevatedCard(
                        cardColors = cardColors,
                        onClick = { onUiAction(InventoryUiAction.OpenItem(it.id)) }
                    ) {
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
