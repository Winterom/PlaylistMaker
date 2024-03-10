package alexey.gritsenko.playlistmaker.model

import alexey.gritsenko.playlistmaker.model.dto.TrackSearchResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackNetworkClient {
    @GET(TrackRepository.SEARCH_TRACK_URL)
   fun findTrack(@Query("term") text: String): Call<TrackSearchResponseDto>
}