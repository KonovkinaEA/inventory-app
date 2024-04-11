package com.example.inventoryapp.ui.screens.identification.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.ui.theme.Blue
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview
import com.example.inventoryapp.ui.theme.White

@Composable
fun MenuInputField(
    label: String,
    value: String,
    readOnly: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    confirmButton: Boolean = false,
    confirm: () -> Unit = {},
    onValueChanged: (String) -> Unit
) {
    val enable = !readOnly || value.isNotEmpty()

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            shape = RoundedCornerShape(5.dp),
            value = value,
            readOnly = readOnly,
            enabled = enable,
            onValueChange = { onValueChanged(it) },
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            colors = ExtendedTheme.textFieldColors,
            singleLine = true,
            modifier = Modifier.weight(1f),
            keyboardOptions = keyboardOptions
        )

        if (confirmButton) {
            OutlinedIconButton(
                enabled = value.isNotEmpty(),
                onClick = { confirm() },
                colors = IconButtonColors(
                    containerColor = Blue,
                    contentColor = White,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = ExtendedTheme.colors.supportSeparator
                ),
                border = if (value.isEmpty()) {
                    BorderStroke(width = 1.dp, color = ExtendedTheme.colors.supportSeparator)
                } else {
                    null
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Confirm icon")
            }
        }
    }
}

@Preview
@Composable
private fun InputFieldPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        Box(modifier = Modifier.background(ExtendedTheme.colors.backSecondary)) {
            MenuInputField(label = "label", value = "value") {}
        }
    }
}
