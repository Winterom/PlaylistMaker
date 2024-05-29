package alexey.gritsenko.playlistmaker.domain.impl

import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.data.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.data.search.TrackRepositoryUseCase
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.OK
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.SERVER_ERROR
import alexey.gritsenko.playlistmaker.domain.search.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import android.os.Handler
import android.os.Looper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class SearchTrackInteractorImpl : SearchTrackInteractor {
    private val trackRepositoryUseCase: TrackRepositoryUseCase
            = ServiceLocator.getService(TrackRepositoryUseCase::class.java)
    private val tracks: MutableList<Track> = ArrayList()
    private val token = Any()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var search:String
    private lateinit var  searchRunnable: Runnable
    companion object{
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }


    override fun findTrack(searchString: String,callbackFunction: (status: RequestStatus) -> Unit) {
        searchRunnable=Runnable{findTrackTask(search,callbackFunction)}
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

    override fun clearTracks() {
        tracks.clear()
    }

    override fun getTrackByPosition(position: Int): Track {
        return tracks[position]
    }

    override fun getCount(): Int {
        return tracks.size
    }

    private fun findTrackTask(searchString: String, callbackFunction: (status: RequestStatus) -> Unit) {
        val rawStrings = searchString.split(" ")
        trackRepositoryUseCase.execute(rawStrings)
            .enqueue(object : Callback<TrackSearchResponseDto> {
                override fun onResponse(
                    call: Call<TrackSearchResponseDto>,
                    response: Response<TrackSearchResponseDto>
                ) {
                    tracks.clear()
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            response.body()!!.results.forEach { tracks.add(Track.convertDtoToEntity(it)) }
                            callbackFunction(OK)
                        } else {
                            tracks.clear()
                            callbackFunction(EMPTY)
                        }
                    } else {
                        tracks.clear()
                        callbackFunction(SERVER_ERROR)
                    }
                }

                override fun onFailure(call: Call<TrackSearchResponseDto>, t: Throwable) {
                    tracks.clear()
                    callbackFunction(NETWORK_ERROR)
                }
            })
    }
}