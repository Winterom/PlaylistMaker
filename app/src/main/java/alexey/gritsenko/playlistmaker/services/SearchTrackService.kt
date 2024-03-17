package alexey.gritsenko.playlistmaker.services

import alexey.gritsenko.playlistmaker.view.TrackListChangedListener



interface SearchTrackService:TrackStoreForAdapter{
    fun addListener(activity: TrackListChangedListener)

    fun deleteListener(activity: TrackListChangedListener)
    fun findTrack(searchString: String)
    fun clearTracks()

}
