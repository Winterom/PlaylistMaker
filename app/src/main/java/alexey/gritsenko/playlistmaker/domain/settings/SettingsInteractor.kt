package alexey.gritsenko.playlistmaker.domain.settings


import alexey.gritsenko.playlistmaker.data.settings.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(isDark:Boolean): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}