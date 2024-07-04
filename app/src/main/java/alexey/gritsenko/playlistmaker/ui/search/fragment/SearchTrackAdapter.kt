package alexey.gritsenko.playlistmaker.ui.search.fragment

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.databinding.TrackItemBinding
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import alexey.gritsenko.playlistmaker.ui.search.view_model.SearchViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchTrackAdapter(private val searchViewModel: SearchViewModel) :
    RecyclerView.Adapter<TrackListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackListViewHolder(TrackItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
       return searchViewModel.getItemCount()
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        val track: Track = searchViewModel.getTrackByPosition(position)
        holder.bind(track)
        holder.itemView.setOnClickListener {
            searchViewModel.addTrackToHistory(track)
            notifyDataSetChanged()
        }
    }
}

class TrackListViewHolder(private val binding:TrackItemBinding )
 : RecyclerView.ViewHolder(binding.root) {
    fun bind(track: Track) {
        binding.trackNameTextView.text = track.trackName
        binding.artistNameTextView.text = track.artistName
        binding.trackTimeTextView.text = track.trackTime
        if (track.artworkUrl100 == null) {
            binding.artworkImageView.setImageResource(R.drawable.placeholder)
        } else {
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .transform(FitCenter(), RoundedCorners(2))
                .placeholder(R.drawable.placeholder)
                .into(binding.artworkImageView)
        }
    }
}

