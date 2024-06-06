package alexey.gritsenko.playlistmaker.domain.player

interface TrackPlayer {
   fun prepare(previewUrl: String, statusObserver: StatusObserver)
    fun play()
    fun pause()
    fun release()
}
