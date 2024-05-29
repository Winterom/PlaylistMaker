package alexey.gritsenko.playlistmaker.ui.searchactivity.activity

import alexey.gritsenko.playlistmaker.AbstractPlayListActivity
import alexey.gritsenko.playlistmaker.PlayListMakerApp
import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.databinding.ActivitySearchBinding
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.CLEAR
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.OK
import alexey.gritsenko.playlistmaker.domain.search.RequestStatus.SERVER_ERROR
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.SearchViewModel
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class SearchActivity : AbstractPlayListActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel


    private lateinit var adapter: SearchTrackAdapter

    private val emptySearchViews = ArrayList<View>()
    private val networkNotAvailableViews = ArrayList<View>()
    private val historyViews = ArrayList<View>()

    companion object {
        const val TEXT_STORED_KEY = "searchText"
    }

    private var searchText: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayListMakerApp.currentActivity=this
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchViewModel =
            ViewModelProvider(
                this,
                SearchViewModel.getViewModelFactory()
            )[SearchViewModel::class.java]
        initView()
        setVisibility()
    }

    private fun initView() {
        binding.trackRecycleView.layoutManager = LinearLayoutManager(this)
        this.adapter =
            SearchTrackAdapter(searchViewModel)
        binding.trackRecycleView.adapter = adapter
        binding.returnToMain.setOnClickListener {
            finish()
        }
        binding.clearText.setOnClickListener {
            binding.searchField.setText("")
            searchViewModel.findTrack("", ::dataIsChanged)
            closeKeyboard()
        }
        binding.searchField.requestFocus()
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val empty = binding.searchField.text.isNullOrEmpty()
                binding.clearText.isVisible = !empty
                if (empty) {
                    closeKeyboard()
                    searchViewModel.clearTracks()
                    adapter.notifyDataSetChanged()
                } else {
                    search(binding.searchField.text.toString())
                }
                if (binding.searchField.hasFocus() && s?.isEmpty() == true) historyShow() else historyHide()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.searchField.addTextChangedListener(simpleTextWatcher)
        binding.searchField.setText(searchText)
        binding.searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchField.text.toString().isNotBlank()) {
                    closeKeyboard()
                    search(binding.searchField.text.toString())
                } else {
                    showToast(getString(R.string.empty_search_field))
                }
            }
            true
        }
        binding.searchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchField.text.isEmpty()) historyShow() else historyHide()
        }
        networkNotAvailableViews.add(binding.internetNotAvailableButtonUpdate)
        networkNotAvailableViews.add(binding.internetNotAvailableImage)
        networkNotAvailableViews.add(binding.internetNotAvailableText)
        emptySearchViews.add(binding.emptySearchImage)
        emptySearchViews.add(binding.emptySearchText)
        historyViews.add(binding.youHistoryText)
        binding.clearHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
            adapter.notifyDataSetChanged()
            historyHide()
        }
        historyViews.add(binding.clearHistoryButton)

        binding.internetNotAvailableButtonUpdate.setOnClickListener {
            if (binding.searchField.text.toString().isNotBlank()) {
                searchViewModel.findTrack(
                    binding.searchField.text.toString(),
                    ::dataIsChanged
                )
            } else {
                showToast(getString(R.string.empty_search_field))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TEXT_STORED_KEY, searchText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(TEXT_STORED_KEY) ?: ""
    }

    private fun closeKeyboard() {
        val inputManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun showKeyboard() {
        if (binding.searchField.requestFocus()) {
            val inputManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(binding.searchField, InputMethodManager.SHOW_IMPLICIT)
            println("keyboard show")
        }
    }

    private fun dataIsChanged(status: RequestStatus) {
        when (status) {
            OK, CLEAR -> okSearchResult()
            EMPTY -> emptySearchResult()
            NETWORK_ERROR -> networkNotAvailable()
            SERVER_ERROR -> serverErrorMessage()
        }
        this.adapter.notifyDataSetChanged()
    }

    private fun showToast(text: String) {
        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
        toast.setGravity(
            Gravity.TOP or Gravity.CENTER,
            0,
            0
        )
        toast.show()
    }

    private fun emptySearchResult() {
        binding.progressBar.isVisible = false
        showKeyboard()
        binding.trackRecycleView.isVisible = false
        this.emptySearchViews.forEach { it.isVisible = true }
        this.networkNotAvailableViews.forEach { it.isVisible = false }
        this.historyViews.forEach { it.isVisible = false }
    }

    private fun okSearchResult() {
        binding.progressBar.isVisible = false
        this.adapter.setShowMode(ShowMode.SHOW_SEARCH_RESULT)
        setTopMargin(binding.trackRecycleView, R.dimen.dimen120dp)
        setHeightConstraint(R.dimen.dimen0dp)
        binding.trackRecycleView.marginTop
        binding.trackRecycleView.isVisible = true
        this.emptySearchViews.forEach { it.isVisible = false }
        this.networkNotAvailableViews.forEach { it.isVisible = false }
        this.historyViews.forEach { it.isVisible = false }
    }

    private fun networkNotAvailable() {
        binding.progressBar.isVisible = false
        closeKeyboard()
        binding.trackRecycleView.isVisible = false
        this.emptySearchViews.forEach { it.isVisible = false }
        this.networkNotAvailableViews.forEach { it.isVisible = true }
        this.historyViews.forEach { it.isVisible = false }
    }

    private fun historyShow() {
        if (searchViewModel.getItemCount(ShowMode.SHOW_HISTORY) == 0) return
        showKeyboard()
        this.adapter.setShowMode(ShowMode.SHOW_HISTORY)
        setTopMargin(binding.trackRecycleView, R.dimen.dimen172dp)
        setHeightConstraint(R.dimen.dimen400dp)
        binding.trackRecycleView.isVisible = true
        this.emptySearchViews.forEach { it.isVisible = false }
        this.networkNotAvailableViews.forEach { it.isVisible = false }
        this.historyViews.forEach { it.isVisible = true }
    }

    private fun historyHide() {
        this.historyViews.forEach { it.isVisible = false }
        binding.trackRecycleView.isVisible = false
    }

    private fun serverErrorMessage() {
        showToast("Что то пошло не так!")
        emptySearchResult()
    }

    private fun setTopMargin(view: View, @DimenRes id: Int) {
        val layoutParams = view.layoutParams as MarginLayoutParams
        val dp = resources.getDimensionPixelSize(id)
        layoutParams.setMargins(0, dp, 0, 0)
    }

    private fun setHeightConstraint(@DimenRes height: Int) {
        val constraint = ConstraintSet()
        constraint.clone(binding.searchLayout)
        val dp = resources.getDimensionPixelSize(height)
        constraint.constrainMaxHeight(id.track_recycle_view, dp)
        constraint.applyTo(binding.searchLayout)
    }

    private fun search(searchString: String) {
        binding.progressBar.isVisible = true
        searchViewModel.findTrack(searchString, ::dataIsChanged)
    }

    private fun setVisibility() {
        if (searchViewModel.getItemCount(ShowMode.SHOW_HISTORY) == 0) {
            historyHide()
            showKeyboard()
        } else {
            historyShow()
        }
        this.emptySearchViews.forEach { it.isVisible = false }
        this.networkNotAvailableViews.forEach { it.isVisible = false }
    }
}