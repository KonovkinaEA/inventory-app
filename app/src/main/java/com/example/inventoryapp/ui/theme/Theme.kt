package com.example.inventoryapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Immutable
data class ExtendedColors(
    val backPrimary: Color = Color.Unspecified,
    val backSecondary: Color = Color.Unspecified,
    val backTertiary: Color = Color.Unspecified,
    val labelPrimary: Color = Color.Unspecified,
    val labelSecondary: Color = Color.Unspecified,
    val labelTertiary: Color = Color.Unspecified,
    val labelDisable: Color = Color.Unspecified,
    val supportSeparator: Color = Color.Unspecified,
)

private val lightExtendedColors = ExtendedColors(
    backPrimary = LightBackPrimary,
    backSecondary = LightBackSecondary,
    backTertiary = LightBackTertiary,
    labelPrimary = LightLabelPrimary,
    labelSecondary = LightLabelSecondary,
    labelTertiary = LightLabelTertiary,
    labelDisable = LightLabelDisable,
    supportSeparator = LightSupportSeparator
)

private val darkExtendedColors = ExtendedColors(
    backPrimary = DarkBackPrimary,
    backSecondary = DarkBackSecondary,
    backTertiary = DarkBackTertiary,
    labelPrimary = DarkLabelPrimary,
    labelSecondary = DarkLabelSecondary,
    labelTertiary = DarkLabelTertiary,
    labelDisable = DarkLabelDisable,
    supportSeparator = DarkSupportSeparator
)

private val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors()
}

@Composable
fun InventoryAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val extendedColors =
        if (darkTheme) darkExtendedColors else lightExtendedColors

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = extendedColors.backPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object ExtendedTheme {

    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current

    val buttonColors: ButtonColors
        @Composable
        get() = ButtonDefaults.buttonColors(
            containerColor = colors.backTertiary,
            contentColor = colors.labelTertiary,
            disabledContainerColor = colors.backSecondary,
            disabledContentColor = colors.supportSeparator
        )
    val cardColors: CardColors
        @Composable
        get() = CardDefaults.cardColors(containerColor = colors.backSecondary)
}
