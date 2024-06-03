package alexey.gritsenko.playlistmaker.data.player.impl


import alexey.gritsenko.playlistmaker.domain.player.StatusObserver
import alexey.gritsenko.playlistmaker.data.player.TrackPlayer
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class TrackPlayerImpl: TrackPlayer {
    companion object{
        private const val DELAY_MILLIS:Long=300
    }

    private val timerHandler = Handler(Looper.getMainLooper())
    private var player:MediaPlayer?=null
    private lateinit var statusObserver: StatusObserver
    private var timerRunnable: Runnable = object : Runnable {
        override fun run() {
            player?.currentPosition?.let { statusObserver.changeTimer(it) }
            timerHandler.postDelayed(this, DELAY_MILLIS)
        }
    }
    override fun prepare(previewUrl: String, statusObserver: StatusObserver,) {

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
        timerHandler.postDelayed(timerRunnable, DELAY_MILLIS)
    }

    override fun pause() {
        player?.pause()
        statusObserver.onPause()
        timerHandler.removeCallbacks(timerRunnable)
    }


    override fun release() {
        timerHandler.removeCallbacks(timerRunnable)
        player?.release()
        player=null
    }


}