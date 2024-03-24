package com.example.open_weater_kotlin_uiui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColors(
    primary = WeatherGreen,
    primaryVariant = WeatherGreenDark,
    secondary = WeatherGreen,
    secondaryVariant = WeatherGreenDark,
    background = Color.White,
    surface = Color.White,
    error = Color.Red,
    onSecondary = Color.White,
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

private val DarkColors = darkColors(
    primary = WeatherGreen,
    primaryVariant = WeatherGreenDark,
    secondary = WeatherGreen,
    secondaryVariant = WeatherGreenDark,
    background = Color.Black,
    surface = Color.Black,
    error = Color.Red,
    onSecondary = Color.Black,
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

@Composable
fun WeatherAndroid2023Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = WeatherTypography,
        content = content
    )
}
