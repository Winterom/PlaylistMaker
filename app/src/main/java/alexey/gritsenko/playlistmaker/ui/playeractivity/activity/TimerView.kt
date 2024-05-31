package alexey.gritsenko.playlistmaker.ui.playeractivity.activity

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import java.util.Locale

class TimerView(private val textView: TextView,
                private val mediaPlayer: MediaPlayer) {
    companion object{
        private const val DELAY_MILLIS:Long=300
    }

    private val timerHandler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable = object : Runnable {
        override fun run() {

            timerHandler.postDelayed(this, DELAY_MILLIS)
        }
    }

    fun start(){
        timerHandler.postDelayed(timerRunnable, DELAY_MILLIS)
    }

    fun stop(){
        timerHandler.removeCallbacks(timerRunnable)
    }

}