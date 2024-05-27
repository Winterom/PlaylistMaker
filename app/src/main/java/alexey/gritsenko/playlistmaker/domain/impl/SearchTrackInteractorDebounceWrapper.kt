package alexey.gritsenko.playlistmaker.domain.impl


import alexey.gritsenko.playlistmaker.domain.search.RequestStatus
import android.os.Handler
import android.os.Looper

class SearchTrackInteractorDebounceWrapper: SearchTrackInteractorImpl() {
    companion object{
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
    private val token = Any()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var search:String
    private lateinit var  searchRunnable: Runnable

    override fun findTrack(searchString: String,callbackFunction: (status: RequestStatus) -> Unit) {
        searchRunnable=Runnable{super.findTrack(search,callbackFunction)}
        if(searchString.isEmpty()){
            cancelSearchRequest()
            return
        }
        this.search = searchString
        handler.removeCallbacks(searchRunnable, token)
        handler.postDelayed(searchRunnable, token, SEARCH_DEBOUNCE_DELAY)
    }

    private fun cancelSearchRequest(){
        this.search =""
        handler.removeCallbacks(searchRunnable, token)
    }
}