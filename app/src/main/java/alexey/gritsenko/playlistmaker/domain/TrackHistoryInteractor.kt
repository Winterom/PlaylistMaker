package alexey.gritsenko.playlistmaker.domain

import alexey.gritsenko.playlistmaker.presentation.searchactivity.HistoryListChangedListener
import alexey.gritsenko.playlistmaker.domain.entity.Track

interface TrackHistoryInteractor {
    fun addListener(activity: HistoryListChangedListener)

    fun deleteListener(activity: HistoryListChangedListener)
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTrackByPosition(position: Int): Track
    fun getCount():Int
}