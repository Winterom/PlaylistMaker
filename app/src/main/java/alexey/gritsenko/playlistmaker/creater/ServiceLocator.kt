package alexey.gritsenko.playlistmaker.creater

import alexey.gritsenko.playlistmaker.PlayListMakerApp
import alexey.gritsenko.playlistmaker.data.impl.TrackRepositoryUseCaseImpl
import alexey.gritsenko.playlistmaker.data.search.TrackRepositoryUseCase
import alexey.gritsenko.playlistmaker.data.settings.SettingsRepository
import alexey.gritsenko.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import alexey.gritsenko.playlistmaker.domain.impl.SearchTrackInteractorImpl
import alexey.gritsenko.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import alexey.gritsenko.playlistmaker.domain.search.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryInteractor
import alexey.gritsenko.playlistmaker.domain.settings.SettingsInteractor
import alexey.gritsenko.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import alexey.gritsenko.playlistmaker.domain.sharing.SharingInteractor
import alexey.gritsenko.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import alexey.gritsenko.playlistmaker.platform.navigator.ExternalNavigator
import alexey.gritsenko.playlistmaker.platform.navigator.impl.ExternalNavigatorImpl
import alexey.gritsenko.playlistmaker.platform.player.TrackPlayer
import alexey.gritsenko.playlistmaker.platform.player.impl.TrackPlayerImpl
import android.app.Application
import android.content.Context

/*Было бы интересно посмотреть как сделать это короче */
//При первом вызове создаестся объект и помещается в мапу
//Что то типа lazy
object ServiceLocator :IServiceLocator{
    private val storage:MutableMap<Class<*>,Any> = HashMap()
    lateinit var application: Application
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalArgumentException::class)
    override fun <T> getService(clazz: Class<T>): T {
        val service = storage[clazz] as T
        if(service !=null) return service
        when(clazz){
            /*Settings*/
            /**********************************************/
            SettingsRepository::class.java-> {
                val newObj=SettingsRepositoryImpl(application.getSharedPreferences(PlayListMakerApp.APP_PREFERENCES, Context.MODE_PRIVATE))
                storage[clazz] = newObj
                return newObj as T
            }
            SettingsInteractor::class.java-> {
                val newObj=SettingsInteractorImpl()
                storage[clazz] = newObj
                return newObj as T
            }
            SharingInteractor::class.java-> {
                val newObj= SharingInteractorImpl()
                storage[clazz] = newObj
                return newObj as T
            }
            ExternalNavigator::class.java->{
                val newObj= ExternalNavigatorImpl(application)
                storage[clazz] = newObj
                return newObj as T
            }
            /*Player*/
            /**********************************************/
            TrackPlayer::class.java->{
                val newObj= TrackPlayerImpl()
                storage[clazz] = newObj
                return newObj as T
            }
            /*Search*/
            /*********************************************/
            TrackHistoryInteractor::class.java->{
                val newObj= TrackHistoryInteractorImpl(application.getSharedPreferences(PlayListMakerApp.APP_PREFERENCES, Context.MODE_PRIVATE))
                storage[clazz] = newObj
                return newObj as T
            }
            SearchTrackInteractor::class.java->{
                val newObj= SearchTrackInteractorImpl()
                storage[clazz] = newObj
                return newObj as T
            }
            TrackRepositoryUseCase::class.java->{
                val newObj= TrackRepositoryUseCaseImpl()
                storage[clazz] = newObj
                return newObj as T
            }
            else-> throw IllegalArgumentException("Объект класса: ${clazz.name} невозможно создать")
        }
    }


}