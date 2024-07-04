package alexey.gritsenko.playlistmaker.ui.media

import alexey.gritsenko.playlistmaker.databinding.FavoritiesTracksFragmentBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : Fragment() {
    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }
    private lateinit var binding: FavoritiesTracksFragmentBinding
    private val viewModel: FavoritesTracksFragmentViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout {
        binding = FavoritiesTracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}