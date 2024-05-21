package alexey.gritsenko.playlistmaker.domain

import alexey.gritsenko.playlistmaker.domain.entity.Track

interface SearchTrackInteractor{
    fun findTrack(searchString: String,callbackFunction: (status:RequestStatus) -> Unit)
    fun clearTracks()
    fun getTrackByPosition(position: Int): Track
    fun getCount():Int
}
