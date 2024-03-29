package alexey.gritsenko.playlistmaker.services

import alexey.gritsenko.playlistmaker.services.entity.Track
import alexey.gritsenko.playlistmaker.activity.searchactivity.HistoryListChangedListener


interface TrackHistoryService {
    fun addListener(activity: HistoryListChangedListener)

    fun deleteListener(activity: HistoryListChangedListener)
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTrackByPosition(position: Int): Track
    fun getCount():Int
}