package alexey.gritsenko.playlistmaker.data.player

import alexey.gritsenko.playlistmaker.domain.player.StatusObserver


interface TrackPlayer {
   fun prepare(previewUrl: String, statusObserver: StatusObserver)
    fun play()
    fun pause()
    fun release()
}
