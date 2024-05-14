package alexey.gritsenko.playlistmaker.domain.impl


import android.os.Handler
import android.os.Looper

class SearchTrackInteractorDebounceWrapper: SearchTrackInteractorImpl() {
    companion object{
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
    private val token = Any()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var search:String
    private var  searchRunnable: Runnable=Runnable{super.findTrack(search)}

    override fun findTrack(searchString: String) {
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