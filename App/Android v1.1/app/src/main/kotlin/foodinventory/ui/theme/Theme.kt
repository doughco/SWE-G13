package com.example.foodinventory.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val XLightColorScheme = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

private val XDarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White
)

private val LGLightColorScheme = lightColorScheme(
    primary = Color(0xFFC62828), // LG Red
    onPrimary = Color.White,
    secondary = Color(0xFF666666),
    onSecondary = Color.White,
    background = Color(0xFFF5F5F5),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

private val LGDarkColorScheme = darkColorScheme(
    primary = Color(0xFFE53935), // Brighter LG Red
    onPrimary = Color.White,
    secondary = Color(0xFFBDBDBD),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)

@Composable
fun XTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) XDarkColorScheme else XLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

@Composable
fun LGTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) LGDarkColorScheme else LGLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
