package alexey.gritsenko.playlistmaker.data

import alexey.gritsenko.playlistmaker.data.dto.TrackSearchResponseDto
import retrofit2.Call


interface TrackRepositoryUseCase {
   fun execute(searchString:List<String>): Call<TrackSearchResponseDto>
}