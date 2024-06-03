package alexey.gritsenko.playlistmaker.domain.search.entity

import alexey.gritsenko.playlistmaker.data.search.dto.TrackSearchResponseDto
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
    val previewUrl:String?,
    val artworkUrl100: String?):Serializable{

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
