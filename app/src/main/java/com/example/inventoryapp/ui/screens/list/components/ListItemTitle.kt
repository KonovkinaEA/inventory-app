package com.example.inventoryapp.ui.screens.list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.inventoryapp.ui.common.MenuElevatedCard
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@Composable
fun MenuTitle(text: String, textColor: Color = ExtendedTheme.colors.labelTertiary) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = textColor,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun TitlePreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        MenuElevatedCard {
            MenuTitle(text = "Title")
        }
    }
}
