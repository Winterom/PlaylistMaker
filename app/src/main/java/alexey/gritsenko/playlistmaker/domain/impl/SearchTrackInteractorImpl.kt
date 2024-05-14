package alexey.gritsenko.playlistmaker.domain.impl

import alexey.gritsenko.playlistmaker.presentation.searchactivity.RequestStatus.CLEAR
import alexey.gritsenko.playlistmaker.presentation.searchactivity.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.presentation.searchactivity.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.presentation.searchactivity.RequestStatus.OK
import alexey.gritsenko.playlistmaker.presentation.searchactivity.RequestStatus.SERVER_ERROR
import alexey.gritsenko.playlistmaker.presentation.searchactivity.TrackListChangedListener
import alexey.gritsenko.playlistmaker.data.TrackRepositoryUseCase
import alexey.gritsenko.playlistmaker.data.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.data.impl.TrackRepositoryUseCaseImpl
import alexey.gritsenko.playlistmaker.domain.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.entity.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.LinkedList

open class SearchTrackInteractorImpl : SearchTrackInteractor {
    private val trackRepositoryUseCase: TrackRepositoryUseCase = TrackRepositoryUseCaseImpl()
    private val tracks: MutableList<Track> = ArrayList()
    private val listeners = LinkedList<TrackListChangedListener>()
    override fun addListener(activity: TrackListChangedListener) {
        this.listeners.add(activity)
    }

    override fun deleteListener(activity: TrackListChangedListener) {
        this.listeners.remove(activity)
    }

    override fun findTrack(searchString: String) {
        val rawStrings = searchString.split(" ")
        trackRepositoryUseCase.execute(rawStrings)
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