package com.example.inventoryapp.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

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
fun MenuCardButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ExtendedTheme.buttonColors,
        shape = RoundedCornerShape(7.dp),
        border = BorderStroke(width = 1.dp, color = ExtendedTheme.colors.supportSeparator),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
    }
}

@Composable
fun MenuInputField(
    label: String,
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        shape = RoundedCornerShape(5.dp),
        value = value,
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
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions
    )
}

@Preview
@Composable
private fun MenuCardPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        MenuElevatedCard {
            MenuCardButton(text = "button") {}
            MenuInputField(label = "label1", value = "") {}
            MenuInputField(label = "label2", value = "value") {}
        }
    }
}
