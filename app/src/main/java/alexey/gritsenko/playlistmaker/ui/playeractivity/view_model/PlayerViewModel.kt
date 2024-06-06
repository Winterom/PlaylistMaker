package alexey.gritsenko.playlistmaker.ui.playeractivity.view_model


import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.player.PlayerInteractor
import alexey.gritsenko.playlistmaker.domain.player.StatusObserver
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerState.COMPLETED
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerState.PAUSE
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerState.STARTED
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import java.util.Locale

class PlayerViewModel : ViewModel() {

    private var playerState= MutableLiveData(COMPLETED)
    private var timerValue = MutableLiveData("0:00")
    private lateinit var playerInteractor:PlayerInteractor

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getViewModelFactory(previewUrl: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val viewModel = PlayerViewModel().apply {
                    playerInteractor=ServiceLocator.getService(PlayerInteractor::class.java)
                    playerInteractor.prepare(previewUrl,statusObserver)
                }
                return viewModel as T
            }
        }
    }
    fun stateLiveData(): LiveData<PlayerState> = playerState

    fun timerLiveData(): LiveData<String> = timerValue
    fun changePlayerState(){
        val value = playerState.value ?: return
        when (value){
            COMPLETED, PAUSE -> playerInteractor.play()
            STARTED -> playerInteractor.pause()
        }
    }

    private val statusObserver: StatusObserver =object:StatusObserver{
        override fun onComplete() {
            playerState.postValue(COMPLETED)
        }

        override fun onPlay() {
            playerState.postValue(STARTED)
        }

        override fun onPause() {
            playerState.postValue(PAUSE)
        }

        override fun changeTimer(newValue: Int) {
            var seconds = newValue/ 1000
            val minutes = seconds / 60
            seconds %= 60
            val value = String.format(Locale("ru"),"%d:%02d", minutes, seconds)
            timerValue.postValue(value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }
}
enum class PlayerState{
    STARTED,PAUSE,COMPLETED
}