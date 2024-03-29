package alexey.gritsenko.playlistmaker.activity.mediaactivity

import alexey.gritsenko.playlistmaker.R.layout
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MediaLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_media_library)
    }
}