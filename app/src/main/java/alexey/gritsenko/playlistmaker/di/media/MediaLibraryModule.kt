package alexey.gritsenko.playlistmaker.di.media

import alexey.gritsenko.playlistmaker.ui.mediaactivity.FavoritesTracksFragmentViewModel
import alexey.gritsenko.playlistmaker.ui.mediaactivity.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibrary= module {

    viewModel {
       FavoritesTracksFragmentViewModel()
    }
    viewModel {
        PlaylistsFragmentViewModel()
    }
}