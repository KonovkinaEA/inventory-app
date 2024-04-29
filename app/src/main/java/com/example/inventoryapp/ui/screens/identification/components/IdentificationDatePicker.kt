package com.example.inventoryapp.ui.screens.identification.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview
import com.example.inventoryapp.ui.theme.White
import com.example.inventoryapp.util.millisToDate
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    editMode: Boolean,
    value: Long?,
    updateDate: (LocalDate) -> Unit,
    clearDate: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            shape = RoundedCornerShape(5.dp),
            value = value?.millisToDate() ?: "",
            readOnly = true,
            enabled = false,
            onValueChange = {},
            label = {
                Text(
                    text = "Дата выпуска",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = ExtendedTheme.colors.labelSecondary,
                unfocusedTextColor = ExtendedTheme.colors.labelSecondary,
                disabledTextColor = ExtendedTheme.colors.labelSecondary,
                focusedBorderColor = ExtendedTheme.colors.supportSeparator,
                unfocusedBorderColor = ExtendedTheme.colors.supportSeparator,
                disabledBorderColor = ExtendedTheme.colors.supportSeparator,
                focusedContainerColor = ExtendedTheme.colors.backSecondary,
                unfocusedContainerColor = ExtendedTheme.colors.backSecondary,
                disabledContainerColor = ExtendedTheme.colors.backSecondary,
                focusedLabelColor = ExtendedTheme.colors.labelDisable,
                unfocusedLabelColor = ExtendedTheme.colors.labelDisable,
                disabledLabelColor = ExtendedTheme.colors.labelDisable
            ),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .clickable { showDatePicker = true }
        )
        OutlinedIconButton(
            enabled = value != null && editMode,
            onClick = { clearDate() },
            colors = IconButtonColors(
                containerColor = Color.Red,
                contentColor = White,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = ExtendedTheme.colors.supportSeparator
            ),
            border = if (value == null || !editMode) {
                BorderStroke(width = 1.dp, color = ExtendedTheme.colors.supportSeparator)
            } else {
                null
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(start = 10.dp, top = 5.dp)
        ) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete icon")
        }
    }

    if (showDatePicker) {
        CalendarDialog(
            state = rememberUseCaseState(visible = true),
            config = CalendarConfig(
                yearSelection = true,
                monthSelection = true,
            ),
            selection = CalendarSelection.Date { updateDate(it) }
        )
    }
}

@Preview
@Composable
private fun DatePickerPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        DatePicker(editMode = true, value = 1714411042567, updateDate = {}) {}
    }
}
