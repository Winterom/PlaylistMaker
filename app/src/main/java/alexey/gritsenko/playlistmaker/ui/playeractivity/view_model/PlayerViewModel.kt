package alexey.gritsenko.playlistmaker.ui.playeractivity.view_model


import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import alexey.gritsenko.playlistmaker.platform.player.StatusObserver
import alexey.gritsenko.playlistmaker.platform.player.TrackPlayer
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerState.COMPLETED
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerState.PAUSE
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerState.PREPARED
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerState.STARTED
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class PlayerViewModel(private val track: Track): ViewModel() {

    private var screenStateMutableLiveData= MutableLiveData(TrackScreenState())
    private val trackPlayer =ServiceLocator.getService(TrackPlayer::class.java)

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val viewModel = PlayerViewModel(track).apply {

                }
                return viewModel as T
            }
        }
    }
    fun getScreenStateLiveData(): LiveData<TrackScreenState> = screenStateMutableLiveData
    fun changePlayerState(){
        val state = screenStateMutableLiveData.value
        when(state?.playerState){
            PREPARED-> track.previewUrl?.let { trackPlayer.play(it,observer) }
            STARTED-> trackPlayer.pause()
            COMPLETED->trackPlayer.release()
            PAUSE->trackPlayer.seek(state.currentPosition)
            null -> return
        }

    }

    private val observer: StatusObserver=object:StatusObserver{
        override fun onProgress(progress: Int) {
            screenStateMutableLiveData.value?.currentPosition=progress
        }

        override fun onStop() {
            screenStateMutableLiveData.value?.playerState=PAUSE
        }

        override fun onPlay() {
            screenStateMutableLiveData.value?.playerState=STARTED
        }
    }

    override fun onCleared() {
        super.onCleared()
        trackPlayer.release()
    }
}
