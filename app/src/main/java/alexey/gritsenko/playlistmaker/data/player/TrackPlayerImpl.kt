package alexey.gritsenko.playlistmaker.data.player


import alexey.gritsenko.playlistmaker.domain.player.StatusObserver
import alexey.gritsenko.playlistmaker.domain.player.TrackPlayer
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class TrackPlayerImpl(private val player:MediaPlayer): TrackPlayer {
    companion object{
        private const val DELAY_MILLIS:Long=300
    }

    private val timerHandler = Handler(Looper.getMainLooper())
    private lateinit var statusObserver: StatusObserver
    private var timerRunnable: Runnable = object : Runnable {
        override fun run() {
            statusObserver.changeTimer(player.currentPosition)
            timerHandler.postDelayed(this, DELAY_MILLIS)
        }
    }
    override fun prepare(previewUrl: String, statusObserver: StatusObserver,) {
        this.statusObserver = statusObserver
        player.setDataSource(previewUrl)
        player.prepareAsync()
        player.setOnCompletionListener {
            timerHandler.removeCallbacks(timerRunnable)
            statusObserver.onComplete()
        }
    }
    override fun play() {
        player.start()
        statusObserver.onPlay()
        timerHandler.postDelayed(timerRunnable, DELAY_MILLIS)
    }

    override fun pause() {
        player.pause()
        statusObserver.onPause()
        timerHandler.removeCallbacks(timerRunnable)
    }


    override fun release() {
        timerHandler.removeCallbacks(timerRunnable)
        player.release()
    }


}