package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SkillspotYellow,
    onPrimary = SkillspotBlack,
    primaryContainer = SkillspotYellowContainer,
    onPrimaryContainer = SkillspotOnYellowContainer,
    secondary = SkillspotYellowBright,
    onSecondary = SkillspotBlack,
    secondaryContainer = SkillspotElevatedSurface,
    onSecondaryContainer = SkillspotYellow,
    tertiary = SkillspotYellowDark,
    onTertiary = SkillspotBlack,
    background = SkillspotBlack,
    onBackground = SkillspotTextPrimary,
    surface = SkillspotDarkSurface,
    onSurface = SkillspotTextPrimary,
    surfaceVariant = SkillspotDarkCard,
    onSurfaceVariant = SkillspotTextSecondary,
    outline = SkillspotOutline,
    outlineVariant = SkillspotOutlineSubtle
)

private val LightColorScheme = darkColorScheme(
    // Defaulting to the high contrast dark/black aesthetic with bold yellow highlights
    primary = SkillspotYellow,
    onPrimary = SkillspotBlack,
    primaryContainer = SkillspotYellowContainer,
    onPrimaryContainer = SkillspotOnYellowContainer,
    secondary = SkillspotYellowBright,
    onSecondary = SkillspotBlack,
    secondaryContainer = SkillspotElevatedSurface,
    onSecondaryContainer = SkillspotYellow,
    tertiary = SkillspotYellowDark,
    onTertiary = SkillspotBlack,
    background = SkillspotBlack,
    onBackground = SkillspotTextPrimary,
    surface = SkillspotDarkSurface,
    onSurface = SkillspotTextPrimary,
    surfaceVariant = SkillspotDarkCard,
    onSurfaceVariant = SkillspotTextSecondary,
    outline = SkillspotOutline,
    outlineVariant = SkillspotOutlineSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve custom brand Black & Yellow (#FFBE00)
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = SkillspotBlack.toArgb()
                it.navigationBarColor = SkillspotDarkSurface.toArgb()
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
