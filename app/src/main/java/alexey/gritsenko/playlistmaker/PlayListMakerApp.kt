package alexey.gritsenko.playlistmaker

import alexey.gritsenko.playlistmaker.di.appModules
import alexey.gritsenko.playlistmaker.domain.settings.SettingsInteractor
import android.app.Application
import android.content.res.Configuration

import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PlayListMakerApp : Application() {

    companion object {
        const val APP_PREFERENCES = "play_list_preferences"
        const val TRACK_HISTORY_KEY = "track_history"
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlayListMakerApp)
            modules(appModules)
        }

        val settingsInteractor: SettingsInteractor = getKoin().get()
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
        val defaultState: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return defaultState == Configuration.UI_MODE_NIGHT_YES
    }
}