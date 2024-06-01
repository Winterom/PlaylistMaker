package alexey.gritsenko.playlistmaker.data.search

import alexey.gritsenko.playlistmaker.data.search.dto.TrackSearchResponseDto
import retrofit2.Call


interface TrackRepositoryUseCase {
   fun execute(searchString:List<String>): Call<TrackSearchResponseDto>
}