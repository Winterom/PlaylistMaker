package alexey.gritsenko.playlistmaker.activity.playeractivity

import android.os.Handler
import android.os.Looper
import android.widget.TextView

class TimerView(private val textView: TextView) {
    private var startTime: Int = 0
    private val timerHandler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable = object : Runnable {
        override fun run() {
            var seconds = startTime
            val minutes = seconds / 60
            seconds %= 60
            startTime++
            textView.text = String.format("%d:%02d", minutes, seconds)
            timerHandler.postDelayed(this, 1000)
        }
    }


    fun start(){
        timerHandler.postDelayed(timerRunnable, 1000)
    }

    fun pause(){
        timerHandler.removeCallbacks(timerRunnable)
    }
}