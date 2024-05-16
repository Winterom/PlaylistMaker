package alexey.gritsenko.playlistmaker.domain


import alexey.gritsenko.playlistmaker.domain.entity.Track

interface TrackHistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTrackByPosition(position: Int): Track
    fun getCount():Int
}