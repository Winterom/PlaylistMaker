package alexey.gritsenko.playlistmaker.data.search.dto

import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Locale

class DtoToEntityMapper {
    companion object{
        fun convertDtoToEntity(dtoResult: TrackSearchResponseDto.SearchResult): Track {
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
                dtoResult.previewUrl,
                dtoResult.artworkUrl100)
        }
    }
}