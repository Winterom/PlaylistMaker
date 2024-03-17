package alexey.gritsenko.playlistmaker.services

import alexey.gritsenko.playlistmaker.services.entity.Track


interface TrackHistoryService:TrackStoreForAdapter {
    fun addTrackToHistory(track: Track)
    fun clearHistory()

}