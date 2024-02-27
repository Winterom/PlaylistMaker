package alexey.gritsenko.playlistmaker.activity

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.services.TrackService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchTrackAdapter(private val trackService: TrackService) :
    RecyclerView.Adapter<SearchTrackAdapter.TrackListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return trackService.getCount()
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        val track = trackService.getTrackByPosition(position)
        holder.trackNameTextView.text = track.trackName
        holder.artistNameTextView.text = track.artistName
        holder.trackTimeTextView.text = track.trackTime
        Glide.with(holder.itemView)
            .load(track.artworkUrl)
            .placeholder(R.drawable.placeholder)
            .into(holder.artworkImageView)
    }

    class TrackListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackNameTextView: TextView = itemView.findViewById(R.id.track_name_text_view)
        val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
        val trackTimeTextView: TextView = itemView.findViewById(R.id.track_time_text_view)
        val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)
    }
}