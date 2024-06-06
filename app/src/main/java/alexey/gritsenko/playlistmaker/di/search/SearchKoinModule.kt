package alexey.gritsenko.playlistmaker.di.search

import alexey.gritsenko.playlistmaker.data.navigator.AppNavigatorImpl
import alexey.gritsenko.playlistmaker.data.search.TrackNetworkClientUseCase
import alexey.gritsenko.playlistmaker.data.search.impl.TrackHistoryRepositoryImpl
import alexey.gritsenko.playlistmaker.data.search.impl.TrackRepositoryUseCaseImpl
import alexey.gritsenko.playlistmaker.domain.search.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryInteractor
import alexey.gritsenko.playlistmaker.domain.search.TrackHistoryRepository
import alexey.gritsenko.playlistmaker.domain.search.TrackRepositoryUseCase
import alexey.gritsenko.playlistmaker.domain.search.impl.SearchTrackInteractorImpl
import alexey.gritsenko.playlistmaker.domain.search.impl.TrackHistoryInteractorImpl
import alexey.gritsenko.playlistmaker.domain.sharing.AppNavigator
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.SearchViewModel

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL: String = "https://itunes.apple.com"

val searchKoinModule = module {
    single<TrackNetworkClientUseCase> {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build().create(TrackNetworkClientUseCase::class.java)
    }
    single<SearchTrackInteractor> {
        SearchTrackInteractorImpl(get())
    }
    single<TrackRepositoryUseCase> {
        TrackRepositoryUseCaseImpl(get())
    }

    single<TrackHistoryInteractor> {
        TrackHistoryInteractorImpl(get())
    }
    single<TrackHistoryRepository> {
        TrackHistoryRepositoryImpl(get(),get())
    }
    single<AppNavigator> {
        AppNavigatorImpl(androidContext())
    }
    viewModel {
        SearchViewModel(get(), get(), get())
    }
}