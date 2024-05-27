package alexey.gritsenko.playlistmaker

import alexey.gritsenko.playlistmaker.creater.ServiceLocator
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
        val theme = settingsInteractor.getThemeSettings(isDarkThemeEnabled()).isDark
        setTheme(theme)
    }
    private fun setTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    private fun isDarkThemeEnabled(): Boolean {
        val defaultState: Int =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return defaultState == Configuration.UI_MODE_NIGHT_YES

    }

}