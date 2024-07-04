package alexey.gritsenko.playlistmaker.ui.media

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.databinding.FragmentMediaLibraryBinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryFragment:Fragment() {

    companion object {
        fun newInstance() = MediaLibraryFragment()
    }
    private lateinit var binding: FragmentMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout {
        super.onCreate(savedInstanceState)
        binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }
}