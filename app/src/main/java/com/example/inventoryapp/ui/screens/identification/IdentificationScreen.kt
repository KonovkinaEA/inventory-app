package com.example.inventoryapp.ui.screens.identification

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.inventoryapp.ui.screens.identification.components.DatePicker
import com.example.inventoryapp.ui.screens.identification.components.IdentificationTopAppBar
import com.example.inventoryapp.ui.screens.identification.model.IdentificationUiAction
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview
import java.time.ZoneOffset

@Composable
fun IdentificationScreen(
    closeScreen: () -> Unit,
    viewModel: IdentificationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.closeScreen.collect { if (it) closeScreen() }
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
        topBar = { IdentificationTopAppBar(state, onUiAction) },
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
                MenuInputField(
                    label = "Штрихкод",
                    value = state.item.barcode,
                    readOnly = false,
                    confirmButton = true,
                    confirm = {
                        onUiAction(IdentificationUiAction.SubmitBarcode)
                        focusManager.clearFocus()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                ) { onUiAction(IdentificationUiAction.UpdateBarcode(it)) }
                MenuInputField(
                    label = "Код",
                    value = state.item.code,
                    readOnly = false,
                    confirmButton = true,
                    confirm = {
                        onUiAction(IdentificationUiAction.SubmitCode)
                        focusManager.clearFocus()
                    }
                ) { onUiAction(IdentificationUiAction.UpdateCode(it)) }
                MenuInputField(
                    label = "Инвентарный номер",
                    value = state.item.inventoryNum,
                    readOnly = false,
                    confirmButton = true,
                    confirm = {
                        onUiAction(IdentificationUiAction.SubmitInventoryNum)
                        focusManager.clearFocus()
                    }
                ) { onUiAction(IdentificationUiAction.UpdateInventoryNumber(it)) }
            }
            MenuElevatedCard {
                if (state.editMode || state.enableDelete) {
                    MenuInputField(
                        label = "Наименование",
                        value = state.item.name,
                        readOnly = !state.editMode
                    ) { onUiAction(IdentificationUiAction.UpdateName(it)) }
                    MenuInputField(
                        label = "Помещение",
                        value = state.item.location,
                        readOnly = !state.editMode
                    ) { onUiAction(IdentificationUiAction.UpdateLocation(it)) }
                    MenuInputField(
                        label = "Количество",
                        value = "${state.item.count ?: ""}",
                        readOnly = !state.editMode
                    ) { onUiAction(IdentificationUiAction.UpdateCount(it.toIntOrNull())) }
                    DatePicker(editMode = state.editMode, state.item.manufactureDate, updateDate = {
                        onUiAction(
                            IdentificationUiAction.UpdateManufactureDate(
                                it.atStartOfDay(
                                    ZoneOffset.UTC
                                ).toInstant().toEpochMilli()
                            )
                        )
                    }) { onUiAction(IdentificationUiAction.UpdateManufactureDate(null)) }
                    MenuInputField(
                        label = "Заводской номер",
                        value = state.item.factoryNum,
                        readOnly = !state.editMode
                    ) { onUiAction(IdentificationUiAction.UpdateFactoryNum(it)) }
                    MenuInputField(
                        label = "Корпус",
                        value = state.item.building,
                        readOnly = !state.editMode
                    ) { onUiAction(IdentificationUiAction.UpdateBuilding(it)) }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    MenuCardButton(
                        text = if (state.editMode) "Отменить" else "Редактировать",
                        modifier = Modifier.weight(1f)
                    ) { onUiAction(IdentificationUiAction.EditMode) }
                    if (state.editMode) {
                        MenuCardButton(
                            text = "Сохранить",
                            enable = state.enableSave,
                            modifier = Modifier.weight(1f)
                        ) { onUiAction(IdentificationUiAction.SaveItem) }
                    }
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
        IdentificationScreenContent(IdentificationUiState(editMode = true)) {}
    }
}
