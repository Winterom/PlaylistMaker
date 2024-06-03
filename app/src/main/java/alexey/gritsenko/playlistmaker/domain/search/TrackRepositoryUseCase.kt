package alexey.gritsenko.playlistmaker.domain.search

import alexey.gritsenko.playlistmaker.data.search.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import retrofit2.Call


interface TrackRepositoryUseCase {
   fun execute(searchString:List<String>, callbackFunction: (status: RequestStatus) -> Unit): List<Track>
}