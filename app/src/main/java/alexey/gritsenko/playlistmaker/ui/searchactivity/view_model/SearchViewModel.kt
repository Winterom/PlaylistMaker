package alexey.gritsenko.playlistmaker.ui.searchactivity.view_model



import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus
import alexey.gritsenko.playlistmaker.domain.search.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryInteractor
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import alexey.gritsenko.playlistmaker.platform.navigator.ExternalNavigator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class SearchViewModel: ViewModel() {
    private lateinit var searchTrackInteractor: SearchTrackInteractor
    private lateinit var trackHistoryInteractor: TrackHistoryInteractor
    private lateinit var externalNavigator: ExternalNavigator
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getViewModelFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val viewModel = SearchViewModel().apply {
                    searchTrackInteractor= ServiceLocator.getService(SearchTrackInteractor::class.java)
                    trackHistoryInteractor = ServiceLocator.getService(TrackHistoryInteractor::class.java)
                    externalNavigator = ServiceLocator.getService(ExternalNavigator::class.java)
                }
                return viewModel as T
            }
        }
    }

    fun findTrack(searchString: String,callbackFunction: (status: RequestStatus) -> Unit){
        searchTrackInteractor.findTrack(searchString,callbackFunction)
    }
    fun clearTracks(){
        searchTrackInteractor.clearTracks()
    }
    fun getItemCount(showMode: ShowMode):Int{
        return when (showMode) {
            ShowMode.SHOW_SEARCH_RESULT -> searchTrackInteractor.getCount()
            ShowMode.SHOW_HISTORY -> trackHistoryInteractor.getCount()
        }
    }
    fun getTrackByPosition(position:Int,showMode: ShowMode):Track{
        return when (showMode) {
            ShowMode.SHOW_SEARCH_RESULT -> searchTrackInteractor.getTrackByPosition(position)
            ShowMode.SHOW_HISTORY -> trackHistoryInteractor.getTrackByPosition(position)
        }
    }
    fun clearHistory(){
        trackHistoryInteractor.clearHistory()
    }
    fun addTrackToHistory(track: Track){
        trackHistoryInteractor.addTrackToHistory(track)
        externalNavigator.startPlayerActivity(track)
    }

}
enum class ShowMode {
    SHOW_SEARCH_RESULT, SHOW_HISTORY
}