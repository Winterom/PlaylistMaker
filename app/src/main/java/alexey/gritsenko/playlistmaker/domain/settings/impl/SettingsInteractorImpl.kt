package alexey.gritsenko.playlistmaker.domain.settings.impl

import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.settings.SettingsInteractor
import alexey.gritsenko.playlistmaker.domain.settings.SettingsRepository
import alexey.gritsenko.playlistmaker.domain.settings.model.ThemeSettings

class SettingsInteractorImpl:SettingsInteractor {
    private val settingsRepository: SettingsRepository = ServiceLocator.getService(
        SettingsRepository::class.java)
    override fun getThemeSettings(isDark: Boolean?): ThemeSettings {
        return settingsRepository.getThemeSettings(isDark)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}