package alexey.gritsenko.playlistmaker.data.search.impl

import alexey.gritsenko.playlistmaker.PlayListMakerApp
import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryRepository
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.LinkedList

class TrackHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences,private val gson:Gson):TrackHistoryRepository {

    companion object {
        private const val HISTORY_CAPACITY = 10
    }

    private val history: LinkedList<Track>


    init {
        val rawHistory = sharedPreferences.getString(PlayListMakerApp.TRACK_HISTORY_KEY, null)
        history = if (rawHistory != null) {
            deserialize(rawHistory)
        } else {
            LinkedList<Track>()
        }
    }
    override fun addTrackToHistory(track: Track) {
        history.remove(track)
        if (history.size == HISTORY_CAPACITY) history.removeLast()
        history.addFirst(track)
        sharedPreferences.edit()
            .putString(PlayListMakerApp.TRACK_HISTORY_KEY, serialize())
            .apply()
    }

    override fun clearHistory() {
        sharedPreferences.edit()
            .remove(PlayListMakerApp.TRACK_HISTORY_KEY)
            .apply()
        history.clear()
    }

    override fun getTrackHistory(): List<Track> {
        return history
    }

    private fun serialize(): String {
        return gson.toJson(history)
    }

    private fun deserialize(raw: String): LinkedList<Track> {
        val type: Type = object : TypeToken<LinkedList<Track>>() {}.type
        return gson.fromJson(raw, type)
    }
}