package alexey.gritsenko.playlistmaker.domain.settings

import alexey.gritsenko.playlistmaker.domain.settings.model.ThemeSettings


interface SettingsInteractor {
    fun getThemeSettings(isDark:Boolean?): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}