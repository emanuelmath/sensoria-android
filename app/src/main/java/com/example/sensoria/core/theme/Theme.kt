package com.example.sensoria.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SensoriaColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = DarkGreen,
    tertiary = VeryDarkGreen,
    background = BackgroundLight,
    surface = BackgroundLight
)

@Composable
fun SensoriaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SensoriaColorScheme,
        typography = SensoriaTypography,
        content = content
    )
}