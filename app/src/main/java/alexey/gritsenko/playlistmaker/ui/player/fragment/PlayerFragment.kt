package alexey.gritsenko.playlistmaker.ui.player.fragment

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.databinding.FragmentPlayerBinding

import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import alexey.gritsenko.playlistmaker.ui.player.view_model.PlayerState
import alexey.gritsenko.playlistmaker.ui.player.view_model.PlayerState.COMPLETED
import alexey.gritsenko.playlistmaker.ui.player.view_model.PlayerState.PAUSE
import alexey.gritsenko.playlistmaker.ui.player.view_model.PlayerViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var track: Track

    companion object {
        const val TIMER_START_VALUE = "0:00"
        fun newInstance(track: Track) {
            val fragment = PlayerFragment()
            fragment.track = track
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        viewModel.prepare(track)
        initViews()
        viewModel.stateLiveData().observe(viewLifecycleOwner) { screeState ->
            if (screeState == null) return@observe
            when (screeState) {
                COMPLETED -> {
                    binding.pauseStartButton.setImageResource(R.drawable.play)
                    binding.trackTimerTextView.text = TIMER_START_VALUE
                }

                PAUSE -> binding.pauseStartButton.setImageResource(R.drawable.play)
                PlayerState.STARTED -> binding.pauseStartButton.setImageResource(R.drawable.pause)
            }
        }
        viewModel.timerLiveData().observe(viewLifecycleOwner) {
            binding.trackTimerTextView.text = it
        }
        return binding.root
    }

    private fun initViews() {
        binding.returnToPrevious.setOnClickListener {
            childFragmentManager.popBackStack()
        }
        binding.playlistIsCreatedTextView.isVisible = false
        binding.pauseStartButton.setImageResource(R.drawable.play)
        binding.pauseStartButton.setOnClickListener {
            viewModel.changePlayerState()
        }
        if (track.artworkUrl100 == null) {
            binding.placeholderImageView.setImageResource(R.drawable.placeholder)
        } else {
            Glide.with(binding.placeholderImageView)
                .load(track.getCoverArtwork())
                .transform(FitCenter())
                .placeholder(R.drawable.placeholder)
                .into(binding.placeholderImageView)
        }
        binding.trackDurationTextView.text = track.trackTime
        binding.trackNameTextView.text = track.trackName
        binding.trackItemArtistTextView.text = track.artistName
        binding.trackYearTextView.text = track.releaseDate
        binding.trackCountryTextView.text = track.country
        binding.trackGenreTextView.text = track.primaryGenreName
        if (track.collectionName == null) {
            binding.trackAlbumTitleTextView.isVisible = false
            binding.trackAlbumTextView.isVisible = false
        } else {
            binding.trackAlbumTextView.text = track.collectionName
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.playerPause()
    }
}