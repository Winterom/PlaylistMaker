package alexey.gritsenko.playlistmaker.model

import alexey.gritsenko.playlistmaker.model.dto.TrackSearchResponseDto
import retrofit2.Call


interface TrackRepository {
   fun findTrack(searchString:List<String>): Call<TrackSearchResponseDto>
}