package alexey.gritsenko.playlistmaker.ui.searchactivity.activity

import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import alexey.gritsenko.playlistmaker.ui.playeractivity.activity.PlayerActivity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity


class StartPlayerActivityByDebounce(private val view: ComponentActivity) {
    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    fun start(track: Track){
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }else{
            return
        }
        val intent = Intent(view, PlayerActivity::class.java)
        intent.putExtra(PlayerActivity.TRACK,track)
        view.startActivity(intent)
    }
}