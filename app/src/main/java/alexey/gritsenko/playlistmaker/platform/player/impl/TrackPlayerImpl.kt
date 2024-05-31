package alexey.gritsenko.playlistmaker.platform.player.impl


import alexey.gritsenko.playlistmaker.domain.player.StatusObserver
import alexey.gritsenko.playlistmaker.platform.player.TrackPlayer
import android.media.MediaPlayer

class TrackPlayerImpl:TrackPlayer {
    private var player:MediaPlayer?=null
    private lateinit var statusObserver: StatusObserver

    override fun prepare(previewUrl: String, statusObserver: StatusObserver) {
        this.statusObserver = statusObserver
        player= MediaPlayer()
        player?.setDataSource(previewUrl)
        player?.prepareAsync()
        player?.setOnCompletionListener {
            statusObserver.onComplete()
        }
    }
    override fun play() {
        player!!.start()
        statusObserver.onPlay()
    }

    override fun pause() {
        player?.pause()
        statusObserver.onPause()
    }


    override fun release() {
        println("УРРРРРРРРРРРРРРРРРРРРРРРРА релиз")
        player?.release()
        player=null
    }

    override fun position(): Int {
        return player!!.currentPosition
    }
}