package alexey.gritsenko.playlistmaker.domain.search

import alexey.gritsenko.playlistmaker.domain.search.entity.Track

interface TrackRepositoryUseCase {
   fun execute(searchString:List<String>, callbackFunction: (status: RequestStatus) -> Unit): List<Track>
}