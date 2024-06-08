package alexey.gritsenko.playlistmaker.domain.search.impl



import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryInteractor
import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryRepository
import alexey.gritsenko.playlistmaker.domain.search.entity.Track


class TrackHistoryInteractorImpl(private val trackHistoryRepository:TrackHistoryRepository) :
    TrackHistoryInteractor {

    override fun addTrackToHistory(track: Track) {
        trackHistoryRepository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        trackHistoryRepository.clearHistory()
    }

    override fun getTrackHistory(): List<Track> {
       return trackHistoryRepository.getTrackHistory()
    }
}