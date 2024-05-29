package alexey.gritsenko.playlistmaker.platform.player.impl

import alexey.gritsenko.playlistmaker.platform.player.StatusObserver
import alexey.gritsenko.playlistmaker.platform.player.TrackPlayer
import android.media.MediaPlayer

class TrackPlayerImpl:TrackPlayer {
    private val player = MediaPlayer()
    private lateinit var statusObserver: StatusObserver
    override fun play(previewUrl: String, statusObserver: StatusObserver) {
        this.statusObserver = statusObserver
        player.setDataSource(previewUrl)
        player.prepareAsync()
        player.start()
        statusObserver.onPlay()
        player.setOnCompletionListener {
            statusObserver.onStop()
        }

    }

    override fun pause() {
        player.pause()
        statusObserver.onStop()
    }

    override fun seek(position: Int) {
        player.seekTo(position)
        statusObserver.onPlay()
    }

    override fun release() {
        player.release()
    }
}