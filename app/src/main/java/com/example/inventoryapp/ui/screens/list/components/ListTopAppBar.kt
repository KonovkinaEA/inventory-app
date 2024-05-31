package com.example.inventoryapp.ui.screens.list.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.ui.screens.list.model.ListUiAction
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopAppBar(location: String, onUiAction: (ListUiAction) -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier.shadow(10.dp),
        title = { Text(text = location, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = { onUiAction(ListUiAction.CloseScreen) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to the Start screen button icon",
                    tint = ExtendedTheme.colors.labelPrimary
                )
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
        ListTopAppBar(location = "123a") {}
    }
}
