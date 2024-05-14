package alexey.gritsenko.playlistmaker.domain.impl

import alexey.gritsenko.playlistmaker.PlayListMakerApp
import alexey.gritsenko.playlistmaker.presentation.searchactivity.HistoryListChangedListener
import alexey.gritsenko.playlistmaker.domain.TrackHistoryInteractor
import alexey.gritsenko.playlistmaker.domain.entity.Track
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.LinkedList

class TrackHistoryInteractorImpl(private val sharedPreferences: SharedPreferences) :
    TrackHistoryInteractor {
    companion object {
        private const val HISTORY_CAPACITY = 10
    }

    private val history: LinkedList<Track>
    private val gson = Gson()
    private val listeners = LinkedList<HistoryListChangedListener>()
    override fun addListener(activity: HistoryListChangedListener) {
        this.listeners.add(activity)
    }

    init {
        val rawHistory = sharedPreferences.getString(PlayListMakerApp.TRACK_HISTORY_KEY, null)
        history = if (rawHistory != null) {
            deserialize(rawHistory)
        } else {
            LinkedList<Track>()
        }
    }

    override fun deleteListener(activity: HistoryListChangedListener) {
        this.listeners.remove(activity)
    }

    override fun addTrackToHistory(track: Track) {
        history.remove(track)
        if (history.size == HISTORY_CAPACITY) history.removeLast()
        history.addFirst(track)
        sharedPreferences.edit()
            .putString(PlayListMakerApp.TRACK_HISTORY_KEY, serialize())
            .apply()
        listeners.forEach{it.historyIsChanged()}
    }

    override fun clearHistory() {
        sharedPreferences.edit()
            .remove(PlayListMakerApp.TRACK_HISTORY_KEY)
            .apply()
        history.clear()
    }

    override fun getTrackByPosition(position: Int): Track {
        return history[position]
    }

    override fun getCount(): Int {
        return history.size
    }

    private fun serialize(): String {
        return gson.toJson(history)
    }

    private fun deserialize(raw: String): LinkedList<Track> {
        val type: Type = object : TypeToken<LinkedList<Track>>() {}.type
        return gson.fromJson(raw, type)
    }
}