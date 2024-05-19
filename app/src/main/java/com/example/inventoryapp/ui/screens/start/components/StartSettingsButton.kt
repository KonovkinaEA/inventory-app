package com.example.inventoryapp.ui.screens.start.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@Composable
fun SettingsButton(openSettings: () -> Unit) {
    FloatingActionButton(
        onClick = { openSettings() },
        containerColor = ExtendedTheme.colors.backSecondary,
        contentColor = ExtendedTheme.colors.labelTertiary,
        shape = RoundedCornerShape(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            modifier = Modifier.size(36.dp),
            contentDescription = "Settings icon"
        )
    }
}

@Preview
@Composable
private fun SettingsButtonPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        SettingsButton {}
    }
}
