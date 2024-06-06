package alexey.gritsenko.playlistmaker.ui.settingactivity.view_model


import alexey.gritsenko.playlistmaker.domain.settings.SettingsInteractor
import alexey.gritsenko.playlistmaker.domain.settings.model.ThemeSettings
import alexey.gritsenko.playlistmaker.domain.sharing.SharingInteractor
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel


class SettingsViewModel(
    private  var sharingInteractor: SharingInteractor,
    private  var settingsInteractor: SettingsInteractor) : ViewModel() {


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
        return settingsInteractor.getThemeSettings(null).isDark
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

}