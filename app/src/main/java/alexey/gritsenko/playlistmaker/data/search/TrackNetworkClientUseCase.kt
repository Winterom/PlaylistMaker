package alexey.gritsenko.playlistmaker.data.search

import alexey.gritsenko.playlistmaker.data.search.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.data.search.impl.TrackRepositoryUseCaseImpl
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackNetworkClientUseCase {
    @GET(TrackRepositoryUseCaseImpl.SEARCH_TRACK_URL)
   fun execute(@Query("term") text: String): Call<TrackSearchResponseDto>
}