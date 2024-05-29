package alexey.gritsenko.playlistmaker.platform.player

interface TrackPlayer {
    fun play(previewUrl: String, statusObserver: StatusObserver)
    fun pause()
    fun seek(position: Int)
    fun release()
}
interface StatusObserver {
    fun onProgress(progress: Int)
    fun onStop()
    fun onPlay()
}