package alexey.gritsenko.playlistmaker.data.settings
import alexey.gritsenko.playlistmaker.data.settings.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(isDark: Boolean?): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}