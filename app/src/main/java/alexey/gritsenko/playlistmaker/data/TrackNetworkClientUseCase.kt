package alexey.gritsenko.playlistmaker.data

import alexey.gritsenko.playlistmaker.data.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.data.impl.TrackRepositoryUseCaseImpl
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackNetworkClientUseCase {
    @GET(TrackRepositoryUseCaseImpl.SEARCH_TRACK_URL)
   fun execute(@Query("term") text: String): Call<TrackSearchResponseDto>
}