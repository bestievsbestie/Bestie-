package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BoosterEjazColorScheme = darkColorScheme(
    primary = MetallicGold,
    onPrimary = VelvetBlack,
    primaryContainer = DarkGold,
    onPrimaryContainer = LightGold,
    secondary = EmeraldGreen,
    onSecondary = VelvetBlack,
    secondaryContainer = DeepEmerald,
    onSecondaryContainer = LightEmerald,
    tertiary = LightGold,
    onTertiary = VelvetBlack,
    background = VelvetBlack,
    onBackground = TextPrimary,
    surface = ObsidianSurface,
    onSurface = TextPrimary,
    surfaceVariant = CardSurface,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder,
    error = ErrorRed
)

@Composable
fun BoosterEjazTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = BoosterEjazColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    BoosterEjazTheme(content = content)
}

