package alexey.gritsenko.playlistmaker.domain.search


import alexey.gritsenko.playlistmaker.domain.search.entity.Track

interface TrackHistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTrackByPosition(position: Int): Track
    fun getCount():Int
}