package alexey.gritsenko.playlistmaker.services

import alexey.gritsenko.playlistmaker.activity.searchactivity.TrackListChangedListener
import alexey.gritsenko.playlistmaker.services.entity.Track

interface SearchTrackService{
    fun addListener(activity: TrackListChangedListener)

    fun deleteListener(activity: TrackListChangedListener)
    fun findTrack(searchString: String)
    fun clearTracks()
    fun getTrackByPosition(position: Int): Track
    fun getCount():Int
}
