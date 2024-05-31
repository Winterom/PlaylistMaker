package alexey.gritsenko.playlistmaker.domain.player



interface PlayerInteractor {
    fun prepare(previewUrl: String, statusObserver: StatusObserver)
    fun play()
    fun pause()
    fun release()
}
interface StatusObserver {
    fun onComplete()
    fun onPlay()
    fun onPause()
}