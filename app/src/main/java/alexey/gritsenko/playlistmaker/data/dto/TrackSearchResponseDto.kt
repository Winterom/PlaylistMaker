package alexey.gritsenko.playlistmaker.data.dto


import java.util.Date

//https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/Searching.html#//apple_ref/doc/uid/TP40017632-CH5-SW1
class TrackSearchResponseDto {
    val resultCount: Int=0
    val results: MutableList<SearchResult> = ArrayList(resultCount)

    class SearchResult{
        val wrapperType: String? = null
        val kind: String? = null
        val artistId: Long = 0
        val collectionId: Long = 0
        val trackId: Long = 0
        val artistName: String? = null
        val collectionName: String? = null
        val trackName: String? = null
        val collectionCensoredName: String? = null
        val trackCensoredName: String? = null
        val artistViewUrl: String? = null
        val collectionViewUrl: String? = null
        val trackViewUrl: String? = null
        val previewUrl: String? = null
        val artworkUrl60: String? = null
        val artworkUrl100: String? = null
        val collectionPrice: String? = null
        val trackPrice: String? = null
        val releaseDate: Date? = null
        val collectionExplicitness: String? = null
        val trackExplicitness: String? = null
        val discCount: Int? = null
        val discNumber: Int? = null
        val trackCount: Int? = null
        val trackNumber: Int? = null
        val trackTimeMillis: Int? = null
        val country: String? = null
        val currency: String? = null
        val primaryGenreName: String? = null
    }
}