package alexey.gritsenko.playlistmaker.ui.settings.fragment

import alexey.gritsenko.playlistmaker.databinding.FragmentSettingsBinding
import alexey.gritsenko.playlistmaker.ui.settings.view_model.SettingsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {
    companion object {
        fun newInstance() = SettingsFragment()
    }
    private val settingsViewModel: SettingsViewModel by viewModel()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): LinearLayout {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {
        binding.themeSwitcher.isChecked = settingsViewModel.getTheme()
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.setTheme(checked)
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