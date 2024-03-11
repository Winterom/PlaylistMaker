package alexey.gritsenko.playlistmaker.services.impl


import alexey.gritsenko.playlistmaker.model.TrackRepository
import alexey.gritsenko.playlistmaker.model.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.model.impl.TrackRepositoryImpl
import alexey.gritsenko.playlistmaker.services.SearchTrackService
import alexey.gritsenko.playlistmaker.services.entity.Track
import alexey.gritsenko.playlistmaker.view.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.view.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.view.RequestStatus.OK
import alexey.gritsenko.playlistmaker.view.RequestStatus.SERVER_ERROR
import alexey.gritsenko.playlistmaker.view.TrackListChangedListener



import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.LinkedList

class SearchTrackServiceImpl:SearchTrackService {
    private val trackRepository: TrackRepository = TrackRepositoryImpl()
    private var tracks:List<Track> = LinkedList<Track>()
    private val listeners = LinkedList<TrackListChangedListener>()
    override fun addListener(activity: TrackListChangedListener) {
        this.listeners.add(activity)
    }

    override fun deleteListener(activity: TrackListChangedListener) {
        this.listeners.remove(activity)
    }

    override fun findTrack(searchString: String) {
        val rawStrings = searchString.split(" ")
        trackRepository.findTrack(rawStrings)
            .enqueue(object: Callback<TrackSearchResponseDto>{
                override fun onResponse(
                    call: Call<TrackSearchResponseDto>,
                    response: Response<TrackSearchResponseDto>
                ) {
                    if(response.code() == 200){
                        if (response.body()?.results?.isNotEmpty() == true){
                            tracks= response.body()!!.results.map {Track.convertDtoToEntity(it)}
                            listeners.forEach{it.dataIsChanged(OK)}
                        }else{
                            tracks = emptyList()
                            listeners.forEach{it.dataIsChanged(EMPTY)}
                        }
                    }else{
                        tracks = emptyList()
                        listeners.forEach{it.dataIsChanged(SERVER_ERROR)}
                    }
                }
                override fun onFailure(call: Call<TrackSearchResponseDto>, t: Throwable) {
                    if (t is HttpException){
                        tracks = emptyList()
                        listeners.forEach{it.dataIsChanged(NETWORK_ERROR)}
                    }

                }

            })
    }

    override fun getTrackByPosition(position: Int): Track {
        return tracks[position]
    }

    override fun getCount(): Int {
        return tracks.size
    }

}