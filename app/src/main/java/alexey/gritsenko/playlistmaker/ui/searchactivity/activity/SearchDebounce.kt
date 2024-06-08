package alexey.gritsenko.playlistmaker.ui.searchactivity.activity

import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.SearchViewModel
import android.os.Handler
import android.os.Looper


class SearchDebounce(private val viewModel: SearchViewModel) {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var  searchRunnable: Runnable

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    fun searchTrack(searchString: String){
        searchRunnable=Runnable{
            viewModel.findTrack(searchString)
        }
        removeCallback()
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun removeCallback(){
        handler.removeCallbacks(searchRunnable)
    }
}