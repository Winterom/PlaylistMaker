package alexey.gritsenko.playlistmaker.domain.impl

import alexey.gritsenko.playlistmaker.data.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.data.impl.TrackRepositoryUseCaseImpl
import alexey.gritsenko.playlistmaker.data.search.TrackRepositoryUseCase
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.OK
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.SERVER_ERROR
import alexey.gritsenko.playlistmaker.domain.search.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class SearchTrackInteractorImpl : SearchTrackInteractor {
    private val trackRepositoryUseCase: TrackRepositoryUseCase = TrackRepositoryUseCaseImpl()
    private val tracks: MutableList<Track> = ArrayList()


    override fun findTrack(searchString: String,callbackFunction: (status: RequestStatus) -> Unit) {
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
                            callbackFunction(OK)
                        } else {
                            tracks.clear()
                            callbackFunction(EMPTY)
                        }
                    } else {
                        tracks.clear()
                        callbackFunction(SERVER_ERROR)
                    }
                }

                override fun onFailure(call: Call<TrackSearchResponseDto>, t: Throwable) {
                    tracks.clear()
                    callbackFunction(NETWORK_ERROR)
                }
            })
    }

    override fun clearTracks() {
        tracks.clear()
    }

    override fun getTrackByPosition(position: Int): Track {
        return tracks[position]
    }

    override fun getCount(): Int {
        return tracks.size
    }
}