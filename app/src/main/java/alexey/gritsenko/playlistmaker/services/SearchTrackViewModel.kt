package alexey.gritsenko.playlistmaker.services

import alexey.gritsenko.playlistmaker.services.entity.Track
import alexey.gritsenko.playlistmaker.view.DataChangedObserver



interface SearchTrackViewModel{
    fun addListener(activity: DataChangedObserver)
    fun deleteListener(activity: DataChangedObserver)
    fun findTrack(searchString: String)
    fun getTrackByPosition(position: Int):Track
    fun getCount():Int
}
