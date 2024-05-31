package alexey.gritsenko.playlistmaker.domain.player



interface PlayerInteractor {
    fun prepare(previewUrl: String, statusObserver: StatusObserver,observer: TimerObserver)
    fun play()
    fun pause()
    fun release()
}
interface StatusObserver {
    fun onComplete()
    fun onPlay()
    fun onPause()
}

interface TimerObserver{
    fun changeValue(newValue:Int)
}