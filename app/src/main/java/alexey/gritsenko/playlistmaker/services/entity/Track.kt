package alexey.gritsenko.playlistmaker.services.entity

import alexey.gritsenko.playlistmaker.model.dto.TrackSearchResponseDto

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String?){
    companion object{
        fun convertDtoToEntity(dtoResult: TrackSearchResponseDto.SearchResult):Track{
            val trackName:String = dtoResult.trackName?:""
            val artistName: String = dtoResult.artistName?:""
            val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(dtoResult.trackTimeMillis)
            return Track(dtoResult.trackId,trackName,artistName,trackTime,dtoResult.artworkUrl100)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Track
        return trackId == other.trackId
    }

    override fun hashCode(): Int {
        return trackId.hashCode()
    }


}
