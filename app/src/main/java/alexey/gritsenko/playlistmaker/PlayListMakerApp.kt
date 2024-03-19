package alexey.gritsenko.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
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
        val savedThemeKey = sharedPreferences.getBoolean(THEME_KEY, false)
        if (savedThemeKey){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
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
}