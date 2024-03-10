package alexey.gritsenko.playlistmaker.view

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.R.layout
import alexey.gritsenko.playlistmaker.services.SearchTrackViewModel
import alexey.gritsenko.playlistmaker.services.entity.Track
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchTrackAdapter(private val searchTrackViewModel: SearchTrackViewModel) :
    RecyclerView.Adapter<TrackListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        return TrackListViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return searchTrackViewModel.getCount()
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        val track = searchTrackViewModel.getTrackByPosition(position)
        holder.bind(track)
    }

}
class TrackListViewHolder(parent: ViewGroup,
    itemView: View = LayoutInflater.from(parent.context).inflate(layout.track_item, parent, false) ) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name_text_view)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_time_text_view)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)
    fun bind(track:Track){
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        trackTimeTextView.text = track.trackTime
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(FitCenter(),RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(artworkImageView)
    }
}