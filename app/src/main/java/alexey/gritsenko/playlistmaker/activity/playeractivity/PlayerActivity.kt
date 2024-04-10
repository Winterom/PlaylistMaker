package alexey.gritsenko.playlistmaker.activity.playeractivity

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.services.entity.Track
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter

class PlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private lateinit var timerTextView:TextView
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
    companion object{
        const val TRACK ="TRACK"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initTrack()
        initViews()
        completeViews()
    }

    private fun initTrack() {
        track = intent.getSerializableExtra(TRACK) as Track
    }
    private fun initViews() {
       findViewById<ImageView>(id.return_to_main)
           .setOnClickListener {
            finish() }
        image = findViewById(id.placeholder_image_view)
        timerTextView = findViewById(id.track_timer_text_view)
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
        timerTextView.text = track.trackTime
        trackGenre.text = track.primaryGenreName
        if(track.collectionName==null){
            albumNameTitle.isVisible=false
            albumName.isVisible=false
        }else{
            albumName.text=track.collectionName
        }
    }

}