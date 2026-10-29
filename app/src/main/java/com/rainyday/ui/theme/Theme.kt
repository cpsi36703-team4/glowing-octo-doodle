package com.rainyday.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val NavyPrimary = Color(0xFF1F5C8B)
val NavyContainer = Color(0xFFD6E4F0)
val OnNavyContainer = Color(0xFF0D283E)

val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = NavyContainer,
    onPrimaryContainer = OnNavyContainer,
    secondary = Color(0xFF3D8FC4),
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A)
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7CB6E6),
    onPrimary = Color(0xFF0B2D47),
    primaryContainer = Color(0xFF1B4262),
    onPrimaryContainer = Color(0xFFD6E4F0),
    secondary = Color(0xFF3D8FC4),
    background = Color(0xFF12181E),
    surface = Color(0xFF1A2128),
    onBackground = Color(0xFFE2E8F0),
    onSurface = Color(0xFFE2E8F0)
)

@Composable
fun RainyDayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
