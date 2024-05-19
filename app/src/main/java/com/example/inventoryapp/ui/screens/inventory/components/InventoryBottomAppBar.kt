package com.example.inventoryapp.ui.screens.inventory.components

import androidx.compose.foundation.layout.padding
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
        MenuCardButton(
            text = "Сканировать штрихкод",
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) { scan() }
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
