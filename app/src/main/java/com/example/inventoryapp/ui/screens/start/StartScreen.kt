package com.example.inventoryapp.ui.screens.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.ui.common.MenuCardButton
import com.example.inventoryapp.ui.common.MenuElevatedCard
import com.example.inventoryapp.ui.theme.ExtendedTheme
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@Composable
fun StartScreen(toIdentification: () -> Unit) {
    StartScreenContent(toIdentification)
}

@Composable
private fun StartScreenContent(toIdentification: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        MenuElevatedCard {
            MenuCardButton(text = "Идентифицировать предмет", onClick = toIdentification)
            MenuCardButton(text = "Начать проверку в аудитории") {}
        }
        MenuElevatedCard {
            MenuCardButton(text = "Получить список предметов в аудитории") {}
            MenuCardButton(text = "Получить список всех предметов") {}
        }
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
