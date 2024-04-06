package com.example.inventoryapp.ui.screens.identification.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@Composable
fun IdentificationAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(
                    text = "ОК", style = MaterialTheme.typography.labelLarge.copy(
                        color = ExtendedTheme.colors.labelTertiary
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(
                    text = "Отмена", style = MaterialTheme.typography.labelLarge.copy(
                        color = ExtendedTheme.colors.labelTertiary
                    )
                )
            }
        },
        icon = {
            Icon(imageVector = Icons.Filled.Info, contentDescription = "Alert dialog icon")
        },
        title = {
            Text(
                text = "Выйти на главный экран?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Все внесенные ранее изменения не сохранятся!",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth()
            )
        },
        containerColor = ExtendedTheme.colors.backPrimary,
        iconContentColor = ExtendedTheme.colors.labelTertiary,
        titleContentColor = ExtendedTheme.colors.labelPrimary,
        textContentColor = ExtendedTheme.colors.labelTertiary
    )
}

@Preview
@Composable
private fun AlertDialogPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        IdentificationAlertDialog(onDismissRequest = {}, onConfirmation = {})
    }
}
