package alexey.gritsenko.playlistmaker.ui.mainactivity

import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.R.layout
import alexey.gritsenko.playlistmaker.ui.mediaactivity.MediaLibraryActivity
import alexey.gritsenko.playlistmaker.ui.searchactivity.activity.SearchActivity
import alexey.gritsenko.playlistmaker.ui.settingactivity.activity.SettingsActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        val searchButton = findViewById<Button>(id.search_button)
        val mediaLibraryButton = findViewById<Button>(id.media_library_button)
        val settingsButton = findViewById<Button>(id.settings_button)
        searchButton.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        mediaLibraryButton.setOnClickListener{
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        settingsButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}