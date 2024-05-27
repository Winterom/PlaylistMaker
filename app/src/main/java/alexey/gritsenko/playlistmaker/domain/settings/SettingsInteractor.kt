package alexey.gritsenko.playlistmaker.domain.settings

import alexey.gritsenko.playlistmaker.data.settings.model.THEME
import alexey.gritsenko.playlistmaker.data.settings.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(currentTheme:THEME?): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}