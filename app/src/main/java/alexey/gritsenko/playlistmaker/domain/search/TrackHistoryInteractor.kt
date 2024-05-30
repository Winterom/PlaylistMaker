package alexey.gritsenko.playlistmaker.domain.search


import alexey.gritsenko.playlistmaker.domain.search.entity.Track

interface TrackHistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTrackHistory():List<Track>
}