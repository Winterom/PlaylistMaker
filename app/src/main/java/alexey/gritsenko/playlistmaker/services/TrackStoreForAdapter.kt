package alexey.gritsenko.playlistmaker.services

import alexey.gritsenko.playlistmaker.services.entity.Track

interface TrackStoreForAdapter {
    fun getTrackByPosition(position: Int): Track
    fun getCount():Int
}