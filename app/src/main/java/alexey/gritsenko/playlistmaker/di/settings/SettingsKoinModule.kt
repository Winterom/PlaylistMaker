package alexey.gritsenko.playlistmaker.di.settings

import alexey.gritsenko.playlistmaker.data.navigator.AppNavigatorImpl
import alexey.gritsenko.playlistmaker.data.settings.SettingsRepositoryImpl
import alexey.gritsenko.playlistmaker.domain.settings.SettingsInteractor
import alexey.gritsenko.playlistmaker.domain.settings.SettingsRepository
import alexey.gritsenko.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import alexey.gritsenko.playlistmaker.domain.sharing.AppNavigator
import alexey.gritsenko.playlistmaker.domain.sharing.SharingInteractor
import alexey.gritsenko.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import alexey.gritsenko.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsInteractor> {
       SettingsInteractorImpl(get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    single<AppNavigator> {
        AppNavigatorImpl(androidContext().applicationContext)
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(get(),get())
    }
    viewModel {
        SettingsViewModel(get(),get())
    }
}
