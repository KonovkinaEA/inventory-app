package com.example.inventoryapp.ui.screens.inventory.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.ui.screens.inventory.InventoryUiState
import com.example.inventoryapp.ui.screens.inventory.model.InventoryUiAction
import com.example.inventoryapp.ui.theme.Blue
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview
import com.example.inventoryapp.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryTopAppBar(state: InventoryUiState, onUiAction: (InventoryUiAction) -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier.shadow(10.dp),
        title = { Text(text = state.location, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = { onUiAction(InventoryUiAction.CloseScreen) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to the Start screen button icon",
                    tint = ExtendedTheme.colors.labelPrimary
                )
            }
        },
        actions = {
            if (!state.endProcess) {
                TextButton(
                    onClick = { onUiAction(InventoryUiAction.EndProcess) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text(
                        text = "Закончить",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ExtendedTheme.colors.backSecondary,
            titleContentColor = ExtendedTheme.colors.labelPrimary
        )
    )
}

@Preview
@Composable
private fun TopAppBarPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        InventoryTopAppBar(state = InventoryUiState(location = "3-405")) {}
    }
}

