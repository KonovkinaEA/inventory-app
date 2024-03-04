package com.example.inventoryapp.ui.theme

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ThemeModePreview : PreviewParameterProvider<Boolean> {

    override val values: Sequence<Boolean> = sequenceOf(
        false,
        true
    )
}
