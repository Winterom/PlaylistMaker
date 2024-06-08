package alexey.gritsenko.playlistmaker.domain.search.impl


import alexey.gritsenko.playlistmaker.domain.search.RequestStatus
import alexey.gritsenko.playlistmaker.domain.search.SearchTrackUseCase
import alexey.gritsenko.playlistmaker.domain.search.TrackRepositoryUseCase
import alexey.gritsenko.playlistmaker.domain.search.entity.Track

class SearchTrackUseCaseImpl(private val trackRepositoryUseCase: TrackRepositoryUseCase) :
    SearchTrackUseCase {

    override fun execute(
        searchString: String,
        callbackFunction: (status: RequestStatus) -> Unit
    ):List<Track> {
        val rawStrings = searchString.split(" ")
       return trackRepositoryUseCase.execute(rawStrings, callbackFunction)

    }


}