package alexey.gritsenko.playlistmaker.di

import alexey.gritsenko.playlistmaker.PlayListMakerApp
import alexey.gritsenko.playlistmaker.di.player.playerKoinModule
import alexey.gritsenko.playlistmaker.di.search.searchKoinModule
import alexey.gritsenko.playlistmaker.di.settings.settingsModule
import android.content.Context
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModules = module {
    single {
        androidContext().getSharedPreferences(PlayListMakerApp.APP_PREFERENCES, Context.MODE_PRIVATE)
    }
    factory { Gson() }
    includes(settingsModule, searchKoinModule, playerKoinModule)
}
