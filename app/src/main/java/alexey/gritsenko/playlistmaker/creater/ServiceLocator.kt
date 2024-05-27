package alexey.gritsenko.playlistmaker.creater

import alexey.gritsenko.playlistmaker.data.settings.SettingsRepository
import alexey.gritsenko.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import alexey.gritsenko.playlistmaker.domain.settings.SettingsInteractor
import alexey.gritsenko.playlistmaker.domain.settings.SharingInteractor
import alexey.gritsenko.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import alexey.gritsenko.playlistmaker.domain.settings.impl.SharingInteractorImpl
import android.content.SharedPreferences

object ServiceLocator :IServiceLocator{
    private val storage:MutableMap<Class<*>,Any> = HashMap()
    fun init(sharedPreferences: SharedPreferences){
        storage[SettingsRepository::class.java] = SettingsRepositoryImpl(sharedPreferences)
        storage[SettingsInteractor::class.java] = SettingsInteractorImpl()
        storage[SharingInteractor::class.java] = SharingInteractorImpl()
    }
    @Suppress("UNCHECKED_CAST")
    override fun <T> getService(clazz: Class<T>): T {
        return storage[clazz] as T


    }


}