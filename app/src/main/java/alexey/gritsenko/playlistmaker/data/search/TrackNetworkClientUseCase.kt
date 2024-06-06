package alexey.gritsenko.playlistmaker.data.search

import alexey.gritsenko.playlistmaker.data.search.dto.TrackSearchResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackNetworkClientUseCase {
    companion object{
        const val SEARCH_TRACK_URL: String ="/search?entity=song"
    }
    @GET(SEARCH_TRACK_URL)
   fun execute(@Query("term") text: String): Call<TrackSearchResponseDto>
}