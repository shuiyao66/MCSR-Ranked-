package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("mcsr_ranked_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_FIRST_LAUNCH = "key_first_launch"
        private const val KEY_LANGUAGE = "key_language"
        private const val KEY_STARTUP_URL = "key_startup_url"
        private const val KEY_THEME_COLOR_ORDINAL = "key_theme_color_ordinal"
        private const val KEY_THEME_MODE_ORDINAL = "key_theme_mode_ordinal"
        private const val KEY_AMOLED_MODE = "key_amoled_mode"
        private const val KEY_BG_IMAGE_URI = "key_bg_image_uri"
        private const val KEY_BG_BLUR_RADIUS = "key_bg_blur_radius"
        private const val KEY_BG_OPACITY = "key_bg_opacity"
        private const val KEY_DESKTOP_MODE = "key_desktop_mode"
        private const val KEY_JAVASCRIPT_ENABLED = "key_javascript_enabled"
        
        const val URL_MCSR_RANKED = "https://mcsrranked.com/"
        const val URL_RANKALYTICS = "https://rankalytics.pages.dev/"
    }

    private val _languageState = MutableStateFlow(getSavedLanguage())
    val languageState: StateFlow<AppLanguage> = _languageState

    private val _themeConfigState = MutableStateFlow(getSavedThemeConfig())
    val themeConfigState: StateFlow<AppThemeConfig> = _themeConfigState

    private val _startupUrlState = MutableStateFlow(getSavedStartupUrl())
    val startupUrlState: StateFlow<String> = _startupUrlState

    private val _desktopModeState = MutableStateFlow(prefs.getBoolean(KEY_DESKTOP_MODE, false))
    val desktopModeState: StateFlow<Boolean> = _desktopModeState

    init {
        // Detect default language on first launch if not saved
        if (!prefs.contains(KEY_LANGUAGE)) {
            val detected = AppLanguage.detectDefault()
            saveLanguage(detected)
        }
    }

    private fun getSavedLanguage(): AppLanguage {
        val code = prefs.getString(KEY_LANGUAGE, null)
        return if (code != null) AppLanguage.fromCode(code) else AppLanguage.detectDefault()
    }

    fun saveLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
        _languageState.value = language
    }

    private fun getSavedStartupUrl(): String {
        return prefs.getString(KEY_STARTUP_URL, URL_MCSR_RANKED) ?: URL_MCSR_RANKED
    }

    fun saveStartupUrl(url: String) {
        prefs.edit().putString(KEY_STARTUP_URL, url).apply()
        _startupUrlState.value = url
    }

    private fun getSavedThemeConfig(): AppThemeConfig {
        val colorOrdinal = prefs.getInt(KEY_THEME_COLOR_ORDINAL, PrimaryThemeColor.MINECRAFT_GREEN.ordinal)
        val modeOrdinal = prefs.getInt(KEY_THEME_MODE_ORDINAL, ThemeMode.DARK.ordinal)
        val isAmoled = prefs.getBoolean(KEY_AMOLED_MODE, false)
        val bgUri = prefs.getString(KEY_BG_IMAGE_URI, null)
        val blurRadius = prefs.getFloat(KEY_BG_BLUR_RADIUS, 10f)
        val opacity = prefs.getFloat(KEY_BG_OPACITY, 0.4f)

        return AppThemeConfig(
            primaryThemeColor = PrimaryThemeColor.fromOrdinal(colorOrdinal),
            themeMode = ThemeMode.values().getOrElse(modeOrdinal) { ThemeMode.DARK },
            isAmoledMode = isAmoled,
            backgroundImageUri = bgUri,
            backgroundBlurRadiusDp = blurRadius,
            backgroundOpacity = opacity
        )
    }

    fun saveThemeConfig(config: AppThemeConfig) {
        prefs.edit()
            .putInt(KEY_THEME_COLOR_ORDINAL, config.primaryThemeColor.ordinal)
            .putInt(KEY_THEME_MODE_ORDINAL, config.themeMode.ordinal)
            .putBoolean(KEY_AMOLED_MODE, config.isAmoledMode)
            .putString(KEY_BG_IMAGE_URI, config.backgroundImageUri)
            .putFloat(KEY_BG_BLUR_RADIUS, config.backgroundBlurRadiusDp)
            .putFloat(KEY_BG_OPACITY, config.backgroundOpacity)
            .apply()

        _themeConfigState.value = config
    }

    fun setDesktopMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DESKTOP_MODE, enabled).apply()
        _desktopModeState.value = enabled
    }
}
