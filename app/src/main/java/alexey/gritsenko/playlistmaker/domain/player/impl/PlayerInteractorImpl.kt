package alexey.gritsenko.playlistmaker.domain.player.impl

import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.player.PlayerInteractor
import alexey.gritsenko.playlistmaker.domain.player.StatusObserver
import alexey.gritsenko.playlistmaker.domain.player.TimerObserver
import alexey.gritsenko.playlistmaker.platform.player.TrackPlayer
import android.os.Handler
import android.os.Looper
import java.util.Locale

class PlayerInteractorImpl:PlayerInteractor {
    companion object{
        private const val DELAY_MILLIS:Long=300
    }
    private val trackPlayer=ServiceLocator.getService(TrackPlayer::class.java)
    private lateinit var timerObserver:TimerObserver
    private val timerHandler = Handler(Looper.getMainLooper())

    private var timerRunnable: Runnable = object : Runnable {
        override fun run() {
            timerObserver.changeValue(trackPlayer.position())
            timerHandler.postDelayed(this, DELAY_MILLIS)
        }
    }
    override fun prepare(previewUrl: String, statusObserver: StatusObserver,observer: TimerObserver) {
        timerObserver=observer
        trackPlayer.prepare(previewUrl,statusObserver)
    }

    override fun play() {
        timerHandler.postDelayed(timerRunnable, DELAY_MILLIS)
        trackPlayer.play()
    }

    override fun pause() {
        timerHandler.removeCallbacks(timerRunnable)
        trackPlayer.pause()
    }

    override fun release() {
        timerHandler.removeCallbacks(timerRunnable)
        trackPlayer.release()
    }

}