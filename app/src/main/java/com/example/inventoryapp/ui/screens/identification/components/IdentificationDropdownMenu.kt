package com.example.inventoryapp.ui.screens.identification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.inventoryapp.data.model.ItemType
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDropdownList(
    selectedOptoin: ItemType,
    editMode: Boolean,
    onSelect: (ItemType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (editMode) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOptoin.uiValue,
            onValueChange = {},
            readOnly = true,
            enabled = editMode || selectedOptoin != ItemType.DEFAULT,
            label = { Text(text = "Тип предмета", style = MaterialTheme.typography.bodyLarge) },
            colors = ExtendedTheme.textFieldColors,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(ExtendedTheme.colors.backSecondary)
        ) {
            ItemType.entries.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.uiValue, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors().copy(
                        textColor = ExtendedTheme.colors.labelSecondary,
                        disabledTextColor = ExtendedTheme.colors.labelSecondary
                    ),
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview
@Composable
private fun DropdownListPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        Box(modifier = Modifier.background(ExtendedTheme.colors.backSecondary)) {
            MenuDropdownList(selectedOptoin = ItemType.SCREEN, editMode = true) {}
        }
    }
}
