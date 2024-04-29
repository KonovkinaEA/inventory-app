package com.example.inventoryapp.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.example.inventoryapp.data.model.InventoryItem
import com.example.inventoryapp.ui.common.MenuElevatedCard
import com.example.inventoryapp.ui.screens.list.components.ListTopAppBar
import com.example.inventoryapp.ui.screens.list.components.MenuTitle
import com.example.inventoryapp.ui.screens.list.model.ListUiAction
import com.example.inventoryapp.ui.screens.list.model.ListUiEvent
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@Composable
fun ListScreen(
    reload: Boolean,
    closeScreen: () -> Unit,
    openItem: (String) -> Unit,
    viewModel: ListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    if (reload) viewModel.loadData()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is ListUiEvent.OpenItem -> openItem(it.id)
                ListUiEvent.CloseScreen -> closeScreen()
            }
        }
    }

    ListScreenContent(state, viewModel::onUiAction)
}

@Composable
private fun ListScreenContent(state: ListUiState, onUiAction: (ListUiAction) -> Unit) {
    Scaffold(
        topBar = { ListTopAppBar(state.location, onUiAction) },
        containerColor = ExtendedTheme.colors.backPrimary
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }
            items(state.list) {
                if (it.name != null) {
                    MenuElevatedCard(
                        onClick = { onUiAction(ListUiAction.OpenItem(it.id)) }
                    ) { MenuTitle(text = it.name) }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListScreenPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    val list = listOf(InventoryItem(name = "name 1"), InventoryItem(name = "name 2"))

    InventoryAppTheme(darkTheme = darkTheme) {
        Box(modifier = Modifier.background(ExtendedTheme.colors.backPrimary)) {
            ListScreenContent(ListUiState(list = list)) {}
        }
    }
}
