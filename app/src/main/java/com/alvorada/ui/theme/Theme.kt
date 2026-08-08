package com.alvorada.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1F6B43),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F4EC),
    onPrimaryContainer = Color(0xFF123A26),
    secondary = Color(0xFF4F6858),
    tertiary = Color(0xFFA66B12),
    background = Color(0xFFF4F6F2),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFEEF1EB),
    outline = Color(0xFF9AA59C),
    outlineVariant = Color(0xFFDCE2D9)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF91D5AA),
    primaryContainer = Color(0xFF245B3B),
    secondary = Color(0xFFB7CCBD),
    tertiary = Color(0xFFF4C46E),
    background = Color(0xFF111713),
    surface = Color(0xFF18201A),
    surfaceVariant = Color(0xFF263029)
)

@Composable
fun AlvoradaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography(),
        content = content
    )
}
