package alexey.gritsenko.playlistmaker.domain.search

import alexey.gritsenko.playlistmaker.domain.search.entity.Track

interface SearchTrackInteractor{
    fun findTrack(searchString: String,callbackFunction: (status: RequestStatus) -> Unit)
    fun clearTracks()
    fun getTrackByPosition(position: Int): Track
    fun getCount():Int
}
