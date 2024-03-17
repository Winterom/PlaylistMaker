package alexey.gritsenko.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import java.util.prefs.AbstractPreferences

class PlayListMakerApp: Application()  {
    companion object{
        const val APP_PREFERENCES = "play_list_preferences"
        const val THEME_KEY = "theme"
    }
    var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

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
    }
}