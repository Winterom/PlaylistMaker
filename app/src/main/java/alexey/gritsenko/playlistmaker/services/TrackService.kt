package alexey.gritsenko.playlistmaker.services

import alexey.gritsenko.playlistmaker.services.models.Track

interface TrackService{
    fun getTrackByPosition(position: Int):Track
    fun getCount():Int
}
