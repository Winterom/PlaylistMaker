package alexey.gritsenko.playlistmaker.ui.mediaactivity

import alexey.gritsenko.playlistmaker.AbstractPlayListActivity
import alexey.gritsenko.playlistmaker.R.layout
import android.os.Bundle

class MediaLibraryActivity : AbstractPlayListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_media_library)
    }
}