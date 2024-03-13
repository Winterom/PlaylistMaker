package alexey.gritsenko.playlistmaker.services.impl

import alexey.gritsenko.playlistmaker.model.TrackRepository
import alexey.gritsenko.playlistmaker.model.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.model.impl.TrackRepositoryImpl
import alexey.gritsenko.playlistmaker.services.SearchTrackService
import alexey.gritsenko.playlistmaker.services.entity.Track
import alexey.gritsenko.playlistmaker.view.RequestStatus.CLEAR
import alexey.gritsenko.playlistmaker.view.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.view.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.view.RequestStatus.OK
import alexey.gritsenko.playlistmaker.view.RequestStatus.SERVER_ERROR
import alexey.gritsenko.playlistmaker.view.TrackListChangedListener

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.LinkedList
import java.util.concurrent.CopyOnWriteArrayList

class SearchTrackServiceImpl : SearchTrackService {
    private val trackRepository: TrackRepository = TrackRepositoryImpl()
    private val tracks: MutableList<Track> = CopyOnWriteArrayList()// Callback -> CompletableFuture
    private val listeners = LinkedList<TrackListChangedListener>()
    override fun addListener(activity: TrackListChangedListener) {
        this.listeners.add(activity)
    }

    override fun deleteListener(activity: TrackListChangedListener) {
        this.listeners.remove(activity)
    }

    override fun findTrack(searchString: String) {
        val rawStrings = searchString.split(" ")
        trackRepository.findTrack(rawStrings)
            .enqueue(object : Callback<TrackSearchResponseDto> {
                override fun onResponse(
                    call: Call<TrackSearchResponseDto>,
                    response: Response<TrackSearchResponseDto>
                ) {
                    tracks.clear()
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                           response.body()!!.results.forEach { tracks.add(Track.convertDtoToEntity(it)) }
                            listeners.forEach { it.dataIsChanged(OK) }
                        } else {
                            tracks.clear()
                            listeners.forEach { it.dataIsChanged(EMPTY) }
                        }
                    } else {
                        tracks.clear()
                        listeners.forEach { it.dataIsChanged(SERVER_ERROR) }
                    }
                }

                override fun onFailure(call: Call<TrackSearchResponseDto>, t: Throwable) {
                    tracks.clear()
                    listeners.forEach { it.dataIsChanged(NETWORK_ERROR) }
                }
            })
    }

    override fun clearTracks() {
        tracks.clear()
        listeners.forEach { it.dataIsChanged(CLEAR) }
    }

    override fun getTrackByPosition(position: Int): Track {
        return tracks[position]
    }

    override fun getCount(): Int {
        return tracks.size
    }
}