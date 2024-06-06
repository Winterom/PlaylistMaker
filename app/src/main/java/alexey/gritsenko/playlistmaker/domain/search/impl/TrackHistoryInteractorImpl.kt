package alexey.gritsenko.playlistmaker.domain.search.impl


import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryInteractor
import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryRepository
import alexey.gritsenko.playlistmaker.domain.search.entity.Track


class TrackHistoryInteractorImpl :
    TrackHistoryInteractor {

        private val trackHistoryRepository:TrackHistoryRepository
        =ServiceLocator.getService(TrackHistoryRepository::class.java)
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