package alexey.gritsenko.playlistmaker.data.search.impl

import alexey.gritsenko.playlistmaker.data.search.TrackNetworkClientUseCase
import alexey.gritsenko.playlistmaker.data.search.dto.DtoToEntityMapper
import alexey.gritsenko.playlistmaker.data.search.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.OK
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.SERVER_ERROR
import alexey.gritsenko.playlistmaker.domain.search.TrackRepositoryUseCase
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TrackRepositoryUseCaseImpl(private val trackNetworkClientUseCase: TrackNetworkClientUseCase) : TrackRepositoryUseCase {



    override fun execute(searchString: List<String>, callbackFunction: (status: RequestStatus) -> Unit): List<Track> {
        val tracks: MutableList<Track> = ArrayList()
        val search:String = if(searchString.size>1){
            searchString.joinToString { "+" }
        }else{
            searchString[0]
        }
        trackNetworkClientUseCase.execute(search).enqueue(object :
            Callback<TrackSearchResponseDto> {
            override fun onResponse(
                call: Call<TrackSearchResponseDto>,
                response: Response<TrackSearchResponseDto>
            ) {

                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        response.body()!!.results.forEach { tracks.add(DtoToEntityMapper.convertDtoToEntity(it)) }
                        callbackFunction(OK)
                    } else {
                        callbackFunction(EMPTY)
                    }
                } else {
                    callbackFunction(SERVER_ERROR)
                }
            }

            override fun onFailure(call: Call<TrackSearchResponseDto>, t: Throwable) {
                callbackFunction(NETWORK_ERROR)
            }
        })
        return tracks
    }


}