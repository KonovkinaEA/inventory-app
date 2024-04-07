package com.example.inventoryapp.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.ui.theme.Blue
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview
import com.example.inventoryapp.ui.theme.White

@Composable
fun MenuElevatedCard(content: @Composable (ColumnScope.() -> Unit)) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = ExtendedTheme.cardColors,
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

@Composable
fun MenuCardButton(
    text: String,
    enable: Boolean = true,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit
) {
    TextButton(
        onClick = { onClick() },
        colors = ExtendedTheme.buttonColors,
        shape = RoundedCornerShape(7.dp),
        enabled = enable,
        border = BorderStroke(width = 1.dp, color = ExtendedTheme.colors.supportSeparator),
        modifier = modifier
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
    }
}

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
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = ExtendedTheme.colors.labelSecondary,
                unfocusedTextColor = ExtendedTheme.colors.labelSecondary,
                focusedBorderColor = ExtendedTheme.colors.supportSeparator,
                unfocusedBorderColor = ExtendedTheme.colors.supportSeparator,
                focusedContainerColor = ExtendedTheme.colors.backSecondary,
                unfocusedContainerColor = ExtendedTheme.colors.backSecondary,
                focusedLabelColor = ExtendedTheme.colors.labelDisable,
                unfocusedLabelColor = ExtendedTheme.colors.labelDisable
            ),
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
private fun MenuCardPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        MenuElevatedCard {
            MenuCardButton(text = "button") {}
            MenuInputField(label = "label1", value = "", readOnly = false, confirmButton = true) {}
            MenuInputField(label = "label2", value = "") {}
        }
    }
}
