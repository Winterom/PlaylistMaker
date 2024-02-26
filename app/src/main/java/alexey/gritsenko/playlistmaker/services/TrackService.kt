package alexey.gritsenko.playlistmaker.services

import alexey.gritsenko.playlistmaker.services.models.Track

internal interface TrackService{
    fun getTrackById(id: Int):Track
}
