package alexey.gritsenko.playlistmaker.ui.settingactivity.view_model

import alexey.gritsenko.playlistmaker.creater.ServiceLocator

import alexey.gritsenko.playlistmaker.data.settings.model.ThemeSettings
import alexey.gritsenko.playlistmaker.domain.settings.SettingsInteractor
import alexey.gritsenko.playlistmaker.domain.settings.SharingInteractor
import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras

class SettingsViewModel : ViewModel() {
    private lateinit var application: Application
    private lateinit var sharingInteractor: SharingInteractor
    private lateinit var settingsInteractor: SettingsInteractor

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getViewModelFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val viewModel = SettingsViewModel().apply {
                    application = checkNotNull(extras[APPLICATION_KEY])
                    settingsInteractor = ServiceLocator.getService(SettingsInteractor::class.java)
                    sharingInteractor = ServiceLocator.getService(SharingInteractor::class.java)

                }
                return viewModel as T
            }
        }
    }

    fun setTheme(isDarkTheme:Boolean){
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        settingsInteractor.updateThemeSetting(ThemeSettings(isDarkTheme))
    }
    fun  getTheme():Boolean{
        return settingsInteractor.getThemeSettings(isDarkThemeEnabled()).isDark
    }
    private fun isDarkThemeEnabled(): Boolean {
        val defaultState: Int =
            application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return defaultState == Configuration.UI_MODE_NIGHT_YES

    }

}