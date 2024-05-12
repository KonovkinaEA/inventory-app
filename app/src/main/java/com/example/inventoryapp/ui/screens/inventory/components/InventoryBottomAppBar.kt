package com.example.inventoryapp.ui.screens.inventory.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.ui.common.MenuCardButton
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@Composable
fun InventoryBottomAppBar(scan: () -> Unit) {
    BottomAppBar(containerColor = ExtendedTheme.colors.backSecondary) {
        Spacer(modifier = Modifier.width(20.dp))
        MenuCardButton(text = "Сканировать штрихкод") { scan() }
        Spacer(modifier = Modifier.width(20.dp))
    }
}

@Preview
@Composable
private fun TopAppBarPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        InventoryBottomAppBar {}
    }
}
