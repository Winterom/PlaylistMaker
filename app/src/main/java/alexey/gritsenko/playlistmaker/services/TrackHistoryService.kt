package alexey.gritsenko.playlistmaker.services

import alexey.gritsenko.playlistmaker.activity.searchactivity.HistoryListChangedListener
import alexey.gritsenko.playlistmaker.services.entity.Track

interface TrackHistoryService {
    fun addListener(activity: HistoryListChangedListener)

    fun deleteListener(activity: HistoryListChangedListener)
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTrackByPosition(position: Int): Track
    fun getCount():Int
}