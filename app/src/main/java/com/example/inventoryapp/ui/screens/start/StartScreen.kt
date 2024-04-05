package com.example.inventoryapp.ui.screens.start

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
fun StartScreen(toDetailsScreen: () -> Unit) {
    StartScreenContent(toDetailsScreen)
}

@Composable
private fun StartScreenContent(toDetailsScreen: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxHeight()
    ) {
        StartElevatedCard {
            StartButton("Идентифицировать предмет") { toDetailsScreen() }
            StartButton("Начать проверку в аудитории") { toDetailsScreen() }
        }
        StartElevatedCard {
            StartButton("Получить список предметов в аудитории") { toDetailsScreen() }
            StartButton("Получить список всех предметов") { toDetailsScreen() }
        }
    }
}

@Composable
private fun StartElevatedCard(content: @Composable (ColumnScope.() -> Unit)) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = ExtendedTheme.colors.backSecondary),
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
private fun StartButton(text: String, onClick: () -> Unit) {
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

@Preview
@Composable
private fun StartScreenPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        Box(modifier = Modifier.background(ExtendedTheme.colors.backPrimary)) {
            StartScreenContent {}
        }
    }
}
