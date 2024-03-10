package alexey.gritsenko.playlistmaker.services.impl


import alexey.gritsenko.playlistmaker.model.TrackRepository
import alexey.gritsenko.playlistmaker.model.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.model.impl.TrackRepositoryImpl
import alexey.gritsenko.playlistmaker.services.SearchTrackViewModel
import alexey.gritsenko.playlistmaker.services.entity.Track
import alexey.gritsenko.playlistmaker.view.DataChangedObserver

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.LinkedList

class SearchTrackViewModelImpl:SearchTrackViewModel {
    private val trackRepository: TrackRepository = TrackRepositoryImpl()
    private var tracks:List<Track> = LinkedList<Track>()
    private val listeners = LinkedList<DataChangedObserver>()
    override fun addListener(activity: DataChangedObserver) {
        this.listeners.add(activity)
    }

    override fun deleteListener(activity: DataChangedObserver) {
        this.listeners.remove(activity)
    }

    override fun findTrack(searchString: String) {
        trackRepository.findTrack(searchString.split(" "))
            .enqueue(object: Callback<TrackSearchResponseDto>{
                override fun onResponse(
                    call: Call<TrackSearchResponseDto>,
                    response: Response<TrackSearchResponseDto>
                ) {
                    if(response.code() == 200){
                        if (response.body()?.results?.isNotEmpty() == true){
                            tracks= response.body()!!.results.map {Track.convertDtoToEntity(it)}
                        }
                    }else{
                        tracks = emptyList()
                    }
                    listeners.forEach{it.dataIsChanged()}
                }
                override fun onFailure(call: Call<TrackSearchResponseDto>, t: Throwable) {
                    tracks = emptyList()
                    listeners.forEach{it.dataIsChanged()}
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