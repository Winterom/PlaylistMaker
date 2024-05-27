package alexey.gritsenko.playlistmaker.ui.playeractivity

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var playButton: ImageButton
    private lateinit var timerTextView:TimerView
    private lateinit var messageTextView:TextView
    private lateinit var image:ImageView
    private lateinit var duration:TextView
    private lateinit var albumName:TextView
    private lateinit var albumNameTitle:TextView
    private lateinit var trackName:TextView
    private lateinit var artistName:TextView
    private lateinit var trackYear:TextView
    private lateinit var trackCountry:TextView
    private lateinit var trackGenre:TextView
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    companion object{
        const val TRACK ="TRACK"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initTrack()
        initViews()
        completeViews()
        initPlayer()
    }

    private fun initTrack() {
        track = intent.getSerializableExtra(TRACK) as Track
    }
    private fun initViews() {
       findViewById<ImageView>(id.return_to_main)
           .setOnClickListener {
            finish() }
        image = findViewById(id.placeholder_image_view)
        timerTextView = TimerView(findViewById(id.track_timer_text_view),mediaPlayer)
        duration = findViewById(id.track_duration_text_view)
        albumName = findViewById(id.track_album_text_view)
        albumNameTitle = findViewById(id.track_album_title_text_view)
        trackName = findViewById(id.track_name_text_view)
        artistName = findViewById(id.track_item_artist_text_view)
        trackYear = findViewById(id.track_year_text_view)
        trackCountry = findViewById(id.track_country_text_view)
        messageTextView = findViewById(id.playlist_is_created_text_view)
        trackGenre = findViewById(id.track_genre_text_view)
        messageTextView.isVisible=false
        playButton = findViewById(id.pause_start_button)
        playButton.setImageResource(R.drawable.play)
        playButton.setOnClickListener {
            when(playerState) {
                STATE_PLAYING -> {
                    pausePlayer()
                }
                STATE_PREPARED, STATE_PAUSED -> {
                    startPlayer()
                }
            }
        }
    }

    private fun completeViews(){
        if (track.artworkUrl100 == null) {
            image.setImageResource(R.drawable.placeholder)
        } else {
            Glide.with(image)
                .load(track.getCoverArtwork())
                .transform(FitCenter())
                .placeholder(R.drawable.placeholder)
                .into(image)
        }
        duration.text=track.trackTime

        trackName.text=track.trackName
        artistName.text=track.artistName
        trackYear.text=track.releaseDate
        trackCountry.text=track.country
        trackGenre.text = track.primaryGenreName
        if(track.collectionName==null){
            albumNameTitle.isVisible=false
            albumName.isVisible=false
        }else{
            albumName.text=track.collectionName
        }
    }

    private fun initPlayer(){
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            playButton.setImageResource(R.drawable.play)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            playButton.setImageResource(R.drawable.play)
            timerTextView.stop()
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playButton.setImageResource(R.drawable.pause)
        timerTextView.start()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playButton.setImageResource(R.drawable.play)
        timerTextView.stop()
    }
    override fun onDestroy() {
        mediaPlayer.stop()
        timerTextView.stop()
        super.onDestroy()
    }
}