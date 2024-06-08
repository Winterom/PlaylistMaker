package alexey.gritsenko.playlistmaker.domain.settings.impl

import alexey.gritsenko.playlistmaker.domain.settings.SettingsInteractor
import alexey.gritsenko.playlistmaker.domain.settings.SettingsRepository
import alexey.gritsenko.playlistmaker.domain.settings.model.ThemeSettings

class SettingsInteractorImpl( private val settingsRepository: SettingsRepository):SettingsInteractor {

    override fun getThemeSettings(isDark: Boolean?): ThemeSettings {
        return settingsRepository.getThemeSettings(isDark)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}