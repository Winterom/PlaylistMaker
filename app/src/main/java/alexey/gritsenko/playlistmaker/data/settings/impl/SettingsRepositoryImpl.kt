package alexey.gritsenko.playlistmaker.data.settings.impl

import alexey.gritsenko.playlistmaker.data.settings.SettingsRepository
import alexey.gritsenko.playlistmaker.data.settings.model.ThemeSettings
import android.content.SharedPreferences

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences?):SettingsRepository {
    companion object{
        const val THEME_KEY = "darkTheme"

    }

    override fun getThemeSettings(isDark:Boolean?): ThemeSettings {
        var theme = isDark
        if(theme==null){
            theme=false
        }
        return ThemeSettings(
            sharedPreferences!!.getBoolean(THEME_KEY, theme)
        )
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences!!
            .edit()
            .putBoolean(THEME_KEY, settings.isDark)
            .apply()
    }
}