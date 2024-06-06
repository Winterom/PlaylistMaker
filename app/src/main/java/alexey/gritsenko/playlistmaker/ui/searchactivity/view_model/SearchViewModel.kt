package alexey.gritsenko.playlistmaker.ui.searchactivity.view_model



import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.CLEAR
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.OK
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.SERVER_ERROR
import alexey.gritsenko.playlistmaker.domain.search.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryInteractor
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import alexey.gritsenko.playlistmaker.domain.sharing.AppNavigator
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode.NONE
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode.SHOW_HISTORY
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel(private val searchTrackInteractor: SearchTrackInteractor,
    private val trackHistoryInteractor: TrackHistoryInteractor,
    private val appNavigator: AppNavigator): ViewModel() {


    private var showMode: MutableLiveData<ShowMode> = MutableLiveData(SHOW_HISTORY)
    private var tracksHistoryStore: List<Track> =trackHistoryInteractor.getTrackHistory()
    private var tracksSearchStore: List<Track> = emptyList()



    fun getShowMode(): LiveData<ShowMode> = showMode
    fun findTrack(searchString: String) {
        if(searchString.isBlank())return
        searchTrackInteractor.findTrack(searchString) { status ->
            when (status) {
                OK -> {
                    showMode.postValue(ShowMode.SHOW_SEARCH_RESULT)
                    tracksSearchStore=searchTrackInteractor.getSearchResult()
                }
                CLEAR->{showMode.postValue(NONE)}
                EMPTY -> {
                    showMode.postValue(ShowMode.EMPTY_SEARCH_RESULT)
                    tracksSearchStore=emptyList()
                }

                NETWORK_ERROR -> {
                    showMode.postValue(ShowMode.NETWORK_ERROR)
                    tracksSearchStore=emptyList()
                }

                SERVER_ERROR -> {
                    showMode.postValue(ShowMode.SERVER_ERROR)
                    tracksSearchStore=emptyList()
                }
            }
        }
    }


        fun getItemCount(): Int {
            if(showMode.value==SHOW_HISTORY){
                return tracksHistoryStore.size
            }
           return tracksSearchStore.size
        }

        fun getTrackByPosition(position: Int): Track {
            if(showMode.value==SHOW_HISTORY){
                return tracksHistoryStore[position]
            }
            return tracksSearchStore[position]
        }

        fun clearHistory() {
            showMode.value=NONE
            trackHistoryInteractor.clearHistory()
            tracksHistoryStore= emptyList()
        }

        fun clearResultSearch(){
            searchTrackInteractor.clearTracks()
            tracksSearchStore= emptyList()
        }
        fun addTrackToHistory(track: Track) {
            trackHistoryInteractor.addTrackToHistory(track)
            tracksHistoryStore =trackHistoryInteractor.getTrackHistory()
            appNavigator.startPlayerActivity(track)
        }
        fun setShowMode(showMode: ShowMode){
            if(showMode== SHOW_HISTORY && tracksHistoryStore.isEmpty()){
                this.showMode.value= NONE
                return
            }
            this.showMode.value=showMode
        }
    }

enum class ShowMode {
    SHOW_SEARCH_RESULT,
    EMPTY_SEARCH_RESULT,
    SHOW_HISTORY,
    NONE,
    NETWORK_ERROR,
    SERVER_ERROR
}