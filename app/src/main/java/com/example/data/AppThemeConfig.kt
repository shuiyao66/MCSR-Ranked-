package com.example.data

import androidx.compose.ui.graphics.Color

enum class PrimaryThemeColor(val nameRes: String, val primary: Color, val secondary: Color) {
    MINECRAFT_GREEN("Minecraft Green", Color(0xFF2E7D32), Color(0xFF4CAF50)),
    NETHER_CRIMSON("Nether Crimson", Color(0xFFC62828), Color(0xFFE53935)),
    ENDER_PURPLE("Ender Purple", Color(0xFF6A1B9A), Color(0xFFAB47BC)),
    OCEAN_TEAL("Ocean Teal", Color(0xFF00695C), Color(0xFF26A69A)),
    SUNSET_ORANGE("Sunset Orange", Color(0xFFE65100), Color(0xFFFF9800)),
    GOLD_YELLOW("Gold Yellow", Color(0xFFF57F17), Color(0xFFFFB300)),
    CYBER_CYAN("Cyber Cyan", Color(0xFF00838F), Color(0xFF00ACC1)),
    CORAL_PINK("Coral Pink", Color(0xFFAD1457), Color(0xFFEC407A));

    companion object {
        fun fromOrdinal(ordinal: Int): PrimaryThemeColor {
            return values().getOrElse(ordinal) { MINECRAFT_GREEN }
        }
    }
}

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

data class AppThemeConfig(
    val primaryThemeColor: PrimaryThemeColor = PrimaryThemeColor.MINECRAFT_GREEN,
    val themeMode: ThemeMode = ThemeMode.DARK,
    val isAmoledMode: Boolean = false,
    val backgroundImageUri: String? = null,
    val backgroundBlurRadiusDp: Float = 10f, // 0 to 25 dp
    val backgroundOpacity: Float = 0.4f // 0.1 to 1.0
)
