package alexey.gritsenko.playlistmaker.services.entity

import alexey.gritsenko.playlistmaker.model.dto.TrackSearchResponseDto
import java.io.Serializable

import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Locale

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val collectionName:String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl100: String?):Serializable{
    companion object{
        fun convertDtoToEntity(dtoResult: TrackSearchResponseDto.SearchResult):Track{
            val releaseDate = when{
                dtoResult.releaseDate==null ->""
                else -> {dtoResult.releaseDate.
                toInstant().
                    atZone(ZoneId.of("UTC"))
                        .year.toString()}
            }
            val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(dtoResult.trackTimeMillis)
            return Track(dtoResult.trackId,
                dtoResult.trackName?:"",
                dtoResult.artistName?:"",
                trackTime,
                dtoResult.collectionName,
                releaseDate,
                dtoResult.primaryGenreName?:"",
                dtoResult.country?:"",
                dtoResult.artworkUrl100)
        }
    }
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")

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
