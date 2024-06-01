package alexey.gritsenko.playlistmaker.ui.playeractivity.activity

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.databinding.ActivityPlayerBinding
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerState.COMPLETED
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerState.PAUSE
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerState.STARTED
import alexey.gritsenko.playlistmaker.ui.playeractivity.view_model.PlayerViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter

class PlayerActivity :  AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private lateinit var track:Track
    companion object {
        const val TRACK = "TRACK"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        track = intent.getSerializableExtra(TRACK) as Track
        viewModel =
            ViewModelProvider(
                this,
                PlayerViewModel.getViewModelFactory(track.previewUrl!!)
            )[PlayerViewModel::class.java]
        initViews()
        viewModel.stateLiveData().observe(this){screeState->
            if(screeState==null) return@observe
            when(screeState){
                PAUSE,COMPLETED->binding.pauseStartButton.setImageResource(R.drawable.play)
                STARTED->binding.pauseStartButton.setImageResource(R.drawable.pause)
            }
        }
        viewModel.timerLiveData().observe(this){
            binding.trackTimerTextView.text=it
        }
    }

    private fun initViews() {
        binding.returnToMain.setOnClickListener {
            finish()
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




}