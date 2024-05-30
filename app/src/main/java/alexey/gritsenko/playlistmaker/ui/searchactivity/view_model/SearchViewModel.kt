package alexey.gritsenko.playlistmaker.ui.searchactivity.view_model



import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.CLEAR
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.OK
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.SERVER_ERROR
import alexey.gritsenko.playlistmaker.domain.search.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryInteractor
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import alexey.gritsenko.playlistmaker.platform.navigator.ExternalNavigator
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode.SHOW_HISTORY
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class SearchViewModel: ViewModel() {
    private lateinit var searchTrackInteractor: SearchTrackInteractor
    private lateinit var trackHistoryInteractor: TrackHistoryInteractor
    private lateinit var externalNavigator: ExternalNavigator
    private var showMode: MutableLiveData<ShowMode> = MutableLiveData(SHOW_HISTORY)
    private lateinit var tracksHistoryStore: MutableLiveData<List<Track>>
    private var tracksSearchStore: MutableLiveData<List<Track>> = MutableLiveData(emptyList())

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getViewModelFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val viewModel = SearchViewModel().apply {
                    searchTrackInteractor =
                        ServiceLocator.getService(SearchTrackInteractor::class.java)
                    trackHistoryInteractor =
                        ServiceLocator.getService(TrackHistoryInteractor::class.java)
                    externalNavigator = ServiceLocator.getService(ExternalNavigator::class.java)
                    val trackHistory = trackHistoryInteractor.getTrackHistory()
                    if (trackHistory.isEmpty()) {
                        showMode.value = ShowMode.NONE
                        tracksHistoryStore = MutableLiveData(emptyList())
                    } else {
                        showMode.value= SHOW_HISTORY
                        tracksHistoryStore=MutableLiveData(trackHistory)
                    }
                }
                return viewModel as T
            }
        }
    }

    fun getShowMode(): LiveData<ShowMode> = showMode
    fun findTrack(searchString: String) {
        showMode.value = ShowMode.LOADING
        searchTrackInteractor.findTrack(searchString) { status ->
            when (status) {
                OK, CLEAR -> {
                    showMode.postValue(ShowMode.SHOW_SEARCH_RESULT)
                    tracksSearchStore.postValue(searchTrackInteractor.getSearchResult())
                }

                EMPTY -> {
                    showMode.postValue(ShowMode.EMPTY_SEARCH_RESULT)
                    tracksSearchStore.postValue(emptyList())
                }

                NETWORK_ERROR -> {
                    showMode.postValue(ShowMode.NETWORK_ERROR)
                    tracksSearchStore.postValue(emptyList())
                }

                SERVER_ERROR -> {
                    showMode.postValue(ShowMode.SERVER_ERROR)
                    tracksSearchStore.postValue(emptyList())
                }
            }
        }
    }


        fun getItemCount(): Int {
            if(showMode.value==SHOW_HISTORY){
                return tracksHistoryStore.value!!.size
            }
           return tracksSearchStore.value!!.size
        }

        fun getTrackByPosition(position: Int): Track {
            if(showMode.value==SHOW_HISTORY){
                return tracksHistoryStore.value!![position]
            }
            return tracksSearchStore.value!![position]
        }

        fun clearHistory() {
            trackHistoryInteractor.clearHistory()
            tracksHistoryStore.value= emptyList()
        }

        fun clearResultSearch(){
            searchTrackInteractor.clearTracks()
            tracksSearchStore.value= emptyList()
        }
        fun addTrackToHistory(track: Track) {
            trackHistoryInteractor.addTrackToHistory(track)
            externalNavigator.startPlayerActivity(track)
        }
        fun setShowMode(showMode: ShowMode){
            if(showMode== SHOW_HISTORY && tracksHistoryStore.value?.isEmpty() == true){
                this.showMode.value=ShowMode.NONE
            }
            this.showMode.value=showMode
        }
    }

enum class ShowMode {
    SHOW_SEARCH_RESULT,
    EMPTY_SEARCH_RESULT,
    SHOW_HISTORY,
    LOADING,NONE,
    NETWORK_ERROR,
    SERVER_ERROR
}