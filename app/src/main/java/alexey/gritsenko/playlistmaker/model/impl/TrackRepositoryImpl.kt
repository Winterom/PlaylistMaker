package alexey.gritsenko.playlistmaker.model.impl

import alexey.gritsenko.playlistmaker.model.TrackNetworkClient
import alexey.gritsenko.playlistmaker.model.TrackRepository
import alexey.gritsenko.playlistmaker.model.dto.TrackSearchResponseDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TrackRepositoryImpl : TrackRepository {
    private var trackNetworkClient: TrackNetworkClient
    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(TrackRepository.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        trackNetworkClient = retrofit.create(TrackNetworkClient::class.java)
    }


    override fun findTrack(searchString: List<String>): Call<TrackSearchResponseDto> {
        return trackNetworkClient.findTrack(searchString.joinToString { "+" })
    }


}