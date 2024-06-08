package alexey.gritsenko.playlistmaker.data.settings

import alexey.gritsenko.playlistmaker.domain.settings.SettingsRepository
import alexey.gritsenko.playlistmaker.domain.settings.model.ThemeSettings
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences,private val gson :Gson):
    SettingsRepository {
    companion object{
        private const val THEME_KEY = "darkTheme"

    }

    override fun getThemeSettings(isDark:Boolean?): ThemeSettings {
        val rawObject:String
       if(isDark==null){
           rawObject = sharedPreferences.getString(THEME_KEY, null) ?: return ThemeSettings(false)
           return deserialize(rawObject)
       }
       rawObject = sharedPreferences.getString(THEME_KEY, null) ?: return ThemeSettings(isDark)
       return deserialize(rawObject)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences
            .edit()
            .putString(THEME_KEY, serialize(settings))
            .apply()
    }
    private fun serialize(settings: ThemeSettings): String {
        return gson.toJson(settings)
    }

    private fun deserialize(raw: String): ThemeSettings {
        val type: Type = object : TypeToken<ThemeSettings>() {}.type
        return gson.fromJson(raw, type)
    }
}