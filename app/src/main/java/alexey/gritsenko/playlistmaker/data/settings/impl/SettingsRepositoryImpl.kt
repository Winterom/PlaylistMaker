package alexey.gritsenko.playlistmaker.data.settings.impl

import alexey.gritsenko.playlistmaker.data.settings.SettingsRepository
import alexey.gritsenko.playlistmaker.data.settings.model.THEME
import alexey.gritsenko.playlistmaker.data.settings.model.THEME.WHITE
import alexey.gritsenko.playlistmaker.data.settings.model.ThemeSettings
import android.content.SharedPreferences

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences?):SettingsRepository {
    companion object{
        const val THEME_KEY = "darkTheme"

    }

    override fun getThemeSettings(currentTheme:THEME?): ThemeSettings {
        var theme = currentTheme
        if(theme==null){
            theme=WHITE
        }
        return ThemeSettings(
            THEME.valueOf(sharedPreferences!!.getString(THEME_KEY, theme.name)!!)
        )
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences!!
            .edit()
            .putString(THEME_KEY, settings.theme.name)
            .apply()
    }
}