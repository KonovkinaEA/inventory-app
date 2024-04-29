package com.example.inventoryapp.ui.screens.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inventoryapp.ui.common.MenuCardButton
import com.example.inventoryapp.ui.common.MenuElevatedCard
import com.example.inventoryapp.ui.screens.start.components.AuditoriumDialog
import com.example.inventoryapp.ui.screens.start.model.StartUiAction
import com.example.inventoryapp.ui.screens.start.model.StartUiEvent
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@Composable
fun StartScreen(
    toIdentification: () -> Unit,
    toList: (String) -> Unit,
    viewModel: StartViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                StartUiEvent.OpenIdentification -> toIdentification()
                is StartUiEvent.OpenList -> toList(it.location)
            }
        }
    }

    StartScreenContent(state, viewModel::onUiAction)
}

@Composable
private fun StartScreenContent(state: StartUiState, onUiAction: (StartUiAction) -> Unit) {
    var openAuditoriumDialog by remember { mutableStateOf(false) }

    if (openAuditoriumDialog) {
        AuditoriumDialog(
            state.location,
            onValueChanged = { onUiAction(StartUiAction.UpdateLocation(it)) },
            onDismissRequest = {
                openAuditoriumDialog = false
                onUiAction(StartUiAction.ClearLocation)
            },
            onConfirmation = {
                openAuditoriumDialog = false
                onUiAction(StartUiAction.OpenLocationList)
                onUiAction(StartUiAction.ClearLocation)
            }
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        MenuElevatedCard {
            MenuCardButton(text = "Идентифицировать предмет") {
                onUiAction(StartUiAction.OpenIdentification)
            }
            MenuCardButton(text = "Начать проверку в помещении") {}
        }
        MenuElevatedCard {
            MenuCardButton(text = "Получить список предметов в помещении") {
                openAuditoriumDialog = true
            }
            MenuCardButton(text = "Получить список всех предметов") {
                onUiAction(StartUiAction.OpenList)
            }
        }
    }
}

@Preview
@Composable
private fun StartScreenPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        Box(modifier = Modifier.background(ExtendedTheme.colors.backPrimary)) {
            StartScreenContent(StartUiState()) {}
        }
    }
}
