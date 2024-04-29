package com.example.inventoryapp.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
        colors = ExtendedTheme.cardColors
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
fun MenuElevatedCard(onClick: () -> Unit, content: @Composable (ColumnScope.() -> Unit)) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = ExtendedTheme.cardColors,
        modifier = Modifier.padding(horizontal = 20.dp),
        onClick = { onClick() }
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

@Preview
@Composable
private fun MenuCardPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        MenuElevatedCard {
            MenuCardButton(text = "button") {}
        }
    }
}
