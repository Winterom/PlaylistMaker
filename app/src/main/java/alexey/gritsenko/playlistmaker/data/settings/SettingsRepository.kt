package alexey.gritsenko.playlistmaker.data.settings

import alexey.gritsenko.playlistmaker.data.settings.model.THEME
import alexey.gritsenko.playlistmaker.data.settings.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(currentTheme: THEME?): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}