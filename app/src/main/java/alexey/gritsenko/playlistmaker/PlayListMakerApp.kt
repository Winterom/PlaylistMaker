package alexey.gritsenko.playlistmaker

import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.data.settings.model.THEME
import alexey.gritsenko.playlistmaker.data.settings.model.THEME.DARK
import alexey.gritsenko.playlistmaker.domain.settings.SettingsInteractor
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate


class PlayListMakerApp : Application() {
    companion object {
        const val APP_PREFERENCES = "play_list_preferences"
        const val TRACK_HISTORY_KEY = "track_history"

    }

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE))
        val settingsInteractor = ServiceLocator.getService(SettingsInteractor::class.java)
        val theme = settingsInteractor.getThemeSettings(isDarkThemeEnabled())
        setTheme(theme.theme)
    }
    private fun setTheme(theme: THEME) {
        AppCompatDelegate.setDefaultNightMode(
            if (theme== DARK) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    private fun isDarkThemeEnabled(): THEME {
        val defaultState: Int =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return when{
            defaultState == Configuration.UI_MODE_NIGHT_YES->DARK
            else ->THEME.WHITE
        }
    }

}