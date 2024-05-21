package alexey.gritsenko.playlistmaker.presentation.searchactivity

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.R.layout
import alexey.gritsenko.playlistmaker.domain.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.TrackHistoryInteractor
import alexey.gritsenko.playlistmaker.domain.entity.Track
import alexey.gritsenko.playlistmaker.presentation.searchactivity.ShowMode.SHOW_HISTORY
import alexey.gritsenko.playlistmaker.presentation.searchactivity.ShowMode.SHOW_SEARCH_RESULT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchTrackAdapter(
    private val searchTrackInteractor: SearchTrackInteractor,
    private val historyTrackService: TrackHistoryInteractor,
    private val startPlayerActivityByDebounce: StartPlayerActivityByDebounce,

    ) :
    RecyclerView.Adapter<TrackListViewHolder>() {
    private var showMode = SHOW_SEARCH_RESULT

    fun setShowMode(newShowMode: ShowMode) {
        if (showMode == newShowMode) return
        showMode = newShowMode
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        return TrackListViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return when (showMode) {
            SHOW_SEARCH_RESULT -> searchTrackInteractor.getCount()
            SHOW_HISTORY -> historyTrackService.getCount()
        }
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        val track: Track =
            when (showMode) {
                SHOW_SEARCH_RESULT -> searchTrackInteractor.getTrackByPosition(position)
                SHOW_HISTORY -> historyTrackService.getTrackByPosition(position)
            }
        holder.bind(track)
        holder.itemView.setOnClickListener {
            historyTrackService.addTrackToHistory(track)
            startPlayerActivityByDebounce.start(track)
            notifyDataSetChanged()
        }
    }
}

class TrackListViewHolder(
    parent: ViewGroup,
    itemView: View = LayoutInflater.from(parent.context).inflate(layout.track_item, parent, false)
) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name_text_view)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_time_text_view)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)

    fun bind(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        trackTimeTextView.text = track.trackTime
        if (track.artworkUrl100 == null) {
            artworkImageView.setImageResource(R.drawable.placeholder)
        } else {
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .transform(FitCenter(), RoundedCorners(2))
                .placeholder(R.drawable.placeholder)
                .into(artworkImageView)
        }
    }
}

enum class ShowMode {
    SHOW_SEARCH_RESULT, SHOW_HISTORY
}