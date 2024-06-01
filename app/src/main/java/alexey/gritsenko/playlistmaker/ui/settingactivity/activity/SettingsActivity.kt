package alexey.gritsenko.playlistmaker.ui.settingactivity.activity

import alexey.gritsenko.playlistmaker.databinding.ActivitySettingsBinding
import alexey.gritsenko.playlistmaker.ui.settingactivity.view_model.SettingsViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class SettingsActivity: AppCompatActivity() {
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingsViewModel =
            ViewModelProvider(
                this,
                SettingsViewModel.getViewModelFactory()
            )[SettingsViewModel::class.java]
        initialize()
    }

    private fun initialize() {
        binding.themeSwitcher.isChecked = settingsViewModel.getTheme()
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.setTheme(checked)
        }
        binding.returnToMain.setOnClickListener {
            finish()
        }
        binding.shareButton.setOnClickListener {
            settingsViewModel.shareApp()
        }
        binding.supportButton.setOnClickListener {
            settingsViewModel.openSupport()
        }
        binding.offerButton.setOnClickListener {
            settingsViewModel.openTerms()
        }
    }
}

