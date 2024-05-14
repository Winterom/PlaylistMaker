package alexey.gritsenko.playlistmaker.domain

import alexey.gritsenko.playlistmaker.presentation.searchactivity.TrackListChangedListener
import alexey.gritsenko.playlistmaker.domain.entity.Track

interface SearchTrackInteractor{
    fun addListener(activity: TrackListChangedListener)

    fun deleteListener(activity: TrackListChangedListener)
    fun findTrack(searchString: String)
    fun clearTracks()
    fun getTrackByPosition(position: Int): Track
    fun getCount():Int
}
