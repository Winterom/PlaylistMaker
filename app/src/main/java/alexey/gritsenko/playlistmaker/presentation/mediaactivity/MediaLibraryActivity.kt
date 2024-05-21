package alexey.gritsenko.playlistmaker.presentation.mediaactivity

import alexey.gritsenko.playlistmaker.R.layout
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MediaLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_media_library)
    }
}