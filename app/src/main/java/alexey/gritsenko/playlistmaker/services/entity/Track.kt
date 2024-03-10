package alexey.gritsenko.playlistmaker.services.entity

import alexey.gritsenko.playlistmaker.model.dto.TrackSearchResponseDto

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String){
    companion object{
        fun convertDtoToEntity(dtoResult: TrackSearchResponseDto.SearchResult):Track{
            val trackName:String = dtoResult.trackName?:""
            val artistName: String = dtoResult.artistName?:""
            val artworkUrl100: String = dtoResult.artworkUrl100?:"about:blank"
            val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(dtoResult.trackTimeMillis)
            return Track(trackName,artistName,trackTime,artworkUrl100)
        }
    }
}
