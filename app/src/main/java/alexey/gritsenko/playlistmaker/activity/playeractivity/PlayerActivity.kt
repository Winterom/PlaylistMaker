package alexey.gritsenko.playlistmaker.activity.playeractivity

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.services.entity.Track
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private lateinit var timerTextView:TextView
    companion object{
        const val TRACK ="TRACK"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initTrack()
        initReturnButton()
        initPlaceholderImage()
        initTrackName()
        initArtistName()
        initTimerTextView()
        initDurationGroup()
        initAlbumGroup()
        initYearTextView()
        initCountryTextView()
    }

    private fun initTrack() {
        track = intent.getSerializableExtra(TRACK) as Track
    }
    private fun initReturnButton(){
        val returnButton = findViewById<ImageView>(id.return_to_main)
        returnButton.setOnClickListener {
            finish()
        }
        println(track)
    }
    private fun initPlaceholderImage() {
        val image = findViewById<ImageView>(id.placeholder_image_view)
        if (track.artworkUrl100 == null) {
            image.setImageResource(R.drawable.placeholder)
        } else {
            Glide.with(image)
                .load(track.getCoverArtwork())
                .transform(FitCenter())
                .placeholder(R.drawable.placeholder)
                .into(image)
        }
    }
    private fun initTimerTextView(){
        timerTextView = findViewById(id.track_timer_text_view)
        timerTextView.text = "0:00"//mock
    }

    private fun initDurationGroup(){
        val duration = findViewById<TextView>(id.track_duration_text_view)
        duration.text=track.trackTime
    }
    private fun initAlbumGroup(){
        val album = findViewById<TextView>(id.track_album_text_view)
        album.text=track.collectionName
    }
    private fun initTrackName(){
        val name = findViewById<TextView>(id.track_name_text_view)
        name.text=track.trackName
    }
    private fun initArtistName(){
        val artist = findViewById<TextView>(id.track_item_artist_text_view)
        artist.text=track.artistName
    }
    private fun initYearTextView(){
        val year = findViewById<TextView>(id.track_year_text_view)
        year.text=track.releaseDate
    }
    private fun initCountryTextView(){
        val country = findViewById<TextView>(id.track_country_text_view)
        country.text=track.country
    }
}