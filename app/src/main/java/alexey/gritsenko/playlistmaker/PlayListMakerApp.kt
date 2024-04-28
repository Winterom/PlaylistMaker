package alexey.gritsenko.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate


class PlayListMakerApp : Application() {
    companion object {
        const val APP_PREFERENCES = "play_list_preferences"
        const val TRACK_HISTORY_KEY = "track_history"
        const val THEME_KEY = "darkTheme"
    }

    var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val systemTheme = isDarkThemeEnabled()
        val savedThemeKey = sharedPreferences.getBoolean(THEME_KEY, systemTheme)
        switchTheme(savedThemeKey)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences
            .edit()
            .putBoolean(THEME_KEY, darkThemeEnabled)
            .apply()
    }
    private fun isDarkThemeEnabled(): Boolean {
        val defaultState: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return defaultState == Configuration.UI_MODE_NIGHT_YES
    }

}