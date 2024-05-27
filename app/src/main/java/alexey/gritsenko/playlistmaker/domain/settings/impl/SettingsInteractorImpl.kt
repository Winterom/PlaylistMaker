package alexey.gritsenko.playlistmaker.domain.settings.impl

import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.data.settings.SettingsRepository
import alexey.gritsenko.playlistmaker.data.settings.model.THEME
import alexey.gritsenko.playlistmaker.data.settings.model.ThemeSettings
import alexey.gritsenko.playlistmaker.domain.settings.SettingsInteractor

class SettingsInteractorImpl:SettingsInteractor {
    private val settingsRepository: SettingsRepository = ServiceLocator.getService(SettingsRepository::class.java)
    override fun getThemeSettings(currentTheme: THEME?): ThemeSettings {
        return settingsRepository.getThemeSettings(currentTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}