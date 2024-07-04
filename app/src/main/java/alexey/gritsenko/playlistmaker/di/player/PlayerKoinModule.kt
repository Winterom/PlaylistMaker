package alexey.gritsenko.playlistmaker.di.player

import alexey.gritsenko.playlistmaker.data.player.TrackPlayerImpl
import alexey.gritsenko.playlistmaker.domain.player.PlayerInteractor
import alexey.gritsenko.playlistmaker.domain.player.TrackPlayer
import alexey.gritsenko.playlistmaker.domain.player.impl.PlayerInteractorImpl
import alexey.gritsenko.playlistmaker.ui.player.view_model.PlayerViewModel
import android.media.MediaPlayer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerKoinModule= module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    factory {
        MediaPlayer()
    }
    factory <TrackPlayer> {
        TrackPlayerImpl(get())
    }
    viewModel {
        PlayerViewModel(get())
    }
}