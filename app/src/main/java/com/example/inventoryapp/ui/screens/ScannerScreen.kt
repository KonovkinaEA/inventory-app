package com.example.inventoryapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.example.inventoryapp.ui.theme.ThemeModePreview

@Composable
fun ScannerScreen(viewModel: ScannerViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    ScannerScreenContent(state) {
        viewModel.startScanning()
    }
}

@Composable
fun ScannerScreenContent(state: ScannerUiState, onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically)
    ) {
        Text(text = state.details)
        Button(onClick = onButtonClick) {
            Text(text = "Start scanning")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScannerScreenPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    InventoryAppTheme(darkTheme = darkTheme) {
        ScannerScreenContent(state = ScannerUiState(), onButtonClick = {})
    }
}
