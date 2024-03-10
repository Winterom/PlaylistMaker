package alexey.gritsenko.playlistmaker.model

import alexey.gritsenko.playlistmaker.model.dto.TrackSearchResponseDto
import retrofit2.Call


interface TrackRepository {
   companion object {
      const val BASE_URL: String = "https://itunes.apple.com"
      const val SEARCH_TRACK_URL: String ="/search?entity=song"
   }
   fun findTrack(searchString:List<String>): Call<TrackSearchResponseDto>
}