package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.AppThemeConfig
import com.example.data.ThemeMode

@Composable
fun MCSRRankedTheme(
    themeConfig: AppThemeConfig,
    content: @Composable () -> Unit
) {
    val isDark = when (themeConfig.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val primaryColor = themeConfig.primaryThemeColor.primary
    val secondaryColor = themeConfig.primaryThemeColor.secondary

    val colorScheme = if (isDark) {
        val bg = if (themeConfig.isAmoledMode) AmoledBlack else DarkSurface
        val surf = if (themeConfig.isAmoledMode) AmoledBlack else DarkSurfaceVariant

        darkColorScheme(
            primary = primaryColor,
            secondary = secondaryColor,
            background = bg,
            surface = surf,
            surfaceVariant = DarkSurfaceVariant,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFFE2E8F0),
            onSurface = Color(0xFFE2E8F0)
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            secondary = secondaryColor,
            background = LightSurface,
            surface = Color.White,
            surfaceVariant = LightSurfaceVariant,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFF1E293B),
            onSurface = Color(0xFF1E293B)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
