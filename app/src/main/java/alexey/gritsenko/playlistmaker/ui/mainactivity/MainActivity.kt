package alexey.gritsenko.playlistmaker.ui.mainactivity

import alexey.gritsenko.playlistmaker.databinding.ActivityMainBinding
import alexey.gritsenko.playlistmaker.ui.mediaactivity.MediaLibraryActivity
import alexey.gritsenko.playlistmaker.ui.searchactivity.activity.SearchActivity
import alexey.gritsenko.playlistmaker.ui.settingactivity.activity.SettingsActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }
    private fun initView(){
        binding.searchButton.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding.mediaLibraryButton.setOnClickListener{
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        binding.settingsButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}