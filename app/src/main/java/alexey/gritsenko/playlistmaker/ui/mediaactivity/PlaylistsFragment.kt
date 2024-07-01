package alexey.gritsenko.playlistmaker.ui.mediaactivity

import alexey.gritsenko.playlistmaker.databinding.PlaylistFragmentBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    companion object {
        fun newInstance() = PlaylistsFragment()
    }
    private lateinit var binding: PlaylistFragmentBinding
    private val viewModel: PlaylistsFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout {
        binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}