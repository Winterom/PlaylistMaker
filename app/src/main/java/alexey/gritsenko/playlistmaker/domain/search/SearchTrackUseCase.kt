package alexey.gritsenko.playlistmaker.domain.search

import alexey.gritsenko.playlistmaker.domain.search.entity.Track

interface SearchTrackUseCase{
    fun execute(searchString: String, callbackFunction: (status: RequestStatus) -> Unit):List<Track>
}
