package alexey.gritsenko.playlistmaker.services.impl

import alexey.gritsenko.playlistmaker.services.TrackHistoryService
import alexey.gritsenko.playlistmaker.services.entity.Track
import android.content.SharedPreferences
import java.util.LinkedList


class TrackHistoryServiceImpl(private val sharedPreferences: SharedPreferences):TrackHistoryService {
    companion object{
        private const val HISTORY_CAPACITY=10
    }
    private val history = LinkedList<Track>()
    init {

    }
    override fun addTrackToHistory(track: Track) {
        history.remove(track)
        if(history.size== HISTORY_CAPACITY) history.removeLast()
        history.addFirst(track)
    }

    override fun clearHistory() {
        history.clear()
    }

    override fun getTrackByPosition(position: Int): Track {
        return history[position]
    }

    override fun getCount(): Int {
       return history.size
    }
}