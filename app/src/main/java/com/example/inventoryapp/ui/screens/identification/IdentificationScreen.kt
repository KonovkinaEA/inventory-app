package com.example.inventoryapp.ui.screens.identification

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inventoryapp.ui.common.MenuCardButton
import com.example.inventoryapp.ui.common.MenuElevatedCard
import com.example.inventoryapp.ui.common.MenuInputField
import com.example.inventoryapp.ui.screens.identification.components.IdentificationTopAppBar
import com.example.inventoryapp.ui.screens.identification.model.IdentificationUiAction
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@Composable
fun IdentificationScreen(
    onClose: () -> Unit,
    viewModel: IdentificationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.closeScreen.collect { if (it) onClose() }
    }

    IdentificationScreenContent(state, viewModel::onUiAction)
}

@Composable
private fun IdentificationScreenContent(
    state: IdentificationUiState,
    onUiAction: (IdentificationUiAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { IdentificationTopAppBar(state.enableSave, onUiAction) },
        containerColor = ExtendedTheme.colors.backPrimary
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            MenuElevatedCard {
                MenuCardButton(text = "Сканировать штрихкод") {
                    onUiAction(IdentificationUiAction.StartScanning)
                }
            }
            MenuElevatedCard {
                MenuInputField(
                    label = "Штрихкод",
                    value = state.item.barcode,
                    confirmButton = true,
                    confirm = { onUiAction(IdentificationUiAction.SubmitBarcode) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                ) {
                    onUiAction(IdentificationUiAction.UpdateBarcode(it))
                }
                MenuInputField(
                    label = "Код",
                    value = state.item.code,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                ) {
                    onUiAction(IdentificationUiAction.UpdateCode(it))
                }
                MenuInputField(
                    label = "Инвентарный номер",
                    value = state.item.number,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                ) {
                    onUiAction(IdentificationUiAction.UpdateInventoryNumber(it))
                }
                MenuInputField(label = "Аудитория", value = state.item.auditorium) {
                    onUiAction(IdentificationUiAction.UpdateAuditorium(it))
                }
                MenuInputField(label = "Тип предмета", value = state.item.type) {
                    onUiAction(IdentificationUiAction.UpdateType(it))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview
@Composable
private fun IdentificationScreenPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        IdentificationScreenContent(IdentificationUiState()) {}
    }
}