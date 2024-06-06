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
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackRepositoryUseCaseImpl : TrackRepositoryUseCase {
    private var trackNetworkClientUseCase: TrackNetworkClientUseCase
    companion object {
        const val BASE_URL: String = "https://itunes.apple.com"
        const val SEARCH_TRACK_URL: String ="/search?entity=song"
    }
    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        trackNetworkClientUseCase = retrofit.create(TrackNetworkClientUseCase::class.java)
    }


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