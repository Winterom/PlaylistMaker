package alexey.gritsenko.playlistmaker.domain.search.impl


import alexey.gritsenko.playlistmaker.domain.search.RequestStatus
import alexey.gritsenko.playlistmaker.domain.search.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.search.TrackRepositoryUseCase
import alexey.gritsenko.playlistmaker.domain.search.entity.Track

class SearchTrackInteractorImpl(private val trackRepositoryUseCase: TrackRepositoryUseCase) : SearchTrackInteractor {
    private var tracks: List<Track> = emptyList()
    private var search:String =""

    override fun findTrack(searchString: String,callbackFunction: (status: RequestStatus) -> Unit) {
        if(searchString == search){
            callbackFunction.invoke(RequestStatus.CLEAR)
            return
        }
        this.search = searchString
        val rawStrings = searchString.split(" ")
        tracks=trackRepositoryUseCase.execute(rawStrings,callbackFunction)

    }



    override fun clearTracks() {
        tracks= emptyList()
    }

    override fun getSearchResult(): List<Track> {
        return tracks
    }

}