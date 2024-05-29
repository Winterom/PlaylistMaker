package alexey.gritsenko.playlistmaker.ui.searchactivity.view_model



import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus
import alexey.gritsenko.playlistmaker.domain.search.SearchTrackInteractor
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class SearchViewModel: ViewModel() {
    private lateinit var searchTrackInteractor: SearchTrackInteractor
    private val token = Any()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var search:String
    private lateinit var  searchRunnable: Runnable

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        @Suppress("UNCHECKED_CAST")
        fun getViewModelFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val viewModel = SearchViewModel().apply {
                        searchTrackInteractor= ServiceLocator.getService(SearchTrackInteractor::class.java)
                }
                return viewModel as T
            }
        }
    }

    fun findTrack(searchString: String,callbackFunction: (status: RequestStatus) -> Unit) {
        searchRunnable=Runnable{searchTrackInteractor.findTrack(search,callbackFunction)}
        if(searchString.isEmpty()){
            cancelSearchRequest()
            return
        }
        this.search = searchString
        handler.removeCallbacks(searchRunnable, token)
        handler.postDelayed(searchRunnable, token,
            SEARCH_DEBOUNCE_DELAY
        )
    }

    private fun cancelSearchRequest(){
        this.search =""
        handler.removeCallbacks(searchRunnable, token)
    }
}