package alexey.gritsenko.playlistmaker.data.impl

import alexey.gritsenko.playlistmaker.data.dto.TrackSearchResponseDto
import alexey.gritsenko.playlistmaker.data.search.TrackNetworkClientUseCase
import alexey.gritsenko.playlistmaker.data.search.TrackRepositoryUseCase
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
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


    override fun execute(searchString: List<String>): Call<TrackSearchResponseDto> {
        val search:String = if(searchString.size>1){
            searchString.joinToString { "+" }
        }else{
            searchString[0]
        }
        return trackNetworkClientUseCase.execute(search)
    }


}