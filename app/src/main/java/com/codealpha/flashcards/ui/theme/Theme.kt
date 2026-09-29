package com.codealpha.flashcards.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Background = Color(0xFF1B2430)
val BackgroundSoft = Color(0xFF232E3D)
val CardFront = Color(0xFFF5EFE0)
val CardBack = Color(0xFFEFE6CE)
val Ink = Color(0xFF23262B)
val InkSoft = Color(0xFF5B5648)
val Gold = Color(0xFFC9A227)
val GoldDim = Color(0xFF8F7420)
val Teal = Color(0xFF4E7C74)
val LineColor = Color(0xFF33404F)
val Danger = Color(0xFFB5563F)
val TextMuted = Color(0xFF9BA6B4)
val White = Color(0xFFF7F4EC)

private val AppColorScheme = darkColorScheme(
    background = Background,
    surface = BackgroundSoft,
    primary = Gold,
    onPrimary = Color(0xFF1B1500),
    secondary = Teal,
    onBackground = White,
    onSurface = White,
    error = Danger
)

@Composable
fun StudyDeckTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}
