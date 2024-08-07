package alexey.gritsenko.playlistmaker.ui.searchactivity.activity


import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.databinding.ActivitySearchBinding
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.SearchViewModel
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode.EMPTY_SEARCH_RESULT
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode.NONE
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode.SERVER_ERROR
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode.SHOW_HISTORY
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode.SHOW_PROGRESS_BAR
import alexey.gritsenko.playlistmaker.ui.searchactivity.view_model.ShowMode.SHOW_SEARCH_RESULT
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
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var adapter: SearchTrackAdapter

    private val emptySearchViews = ArrayList<View>()
    private val networkNotAvailableViews = ArrayList<View>()
    private val historyViews = ArrayList<View>()

    private val showModeObserver = Observer<ShowMode> { newMode ->
        if (newMode==null){
            return@Observer
        }
        when (newMode) {
            SHOW_SEARCH_RESULT -> okSearchResult()
            EMPTY_SEARCH_RESULT -> emptySearchResult()
            SHOW_HISTORY -> historyShow()
            NONE -> showNone()
            NETWORK_ERROR -> networkNotAvailable()
            SERVER_ERROR -> serverErrorMessage()
            SHOW_PROGRESS_BAR->binding.progressBar.isVisible=true
        }

    }
    private var lastSearch:String?=null
    private lateinit var  searchDebounce:SearchDebounce
    companion object {
        const val TEXT_STORED_KEY = "searchText"
    }

    private var searchText: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        searchViewModel.getShowMode().observe(this,showModeObserver)
        searchDebounce = SearchDebounce(searchViewModel)
    }

    private fun initView() {
        binding.trackRecycleView.layoutManager = LinearLayoutManager(this)
        this.adapter =
            SearchTrackAdapter(searchViewModel)
        binding.trackRecycleView.adapter = adapter
        binding.returnToPrevious.setOnClickListener {
            finish()
        }
        binding.clearText.setOnClickListener {
            binding.searchField.setText("")
            searchDebounce.removeCallback()
            closeKeyboard()
            searchViewModel.setShowMode(SHOW_HISTORY)
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
                    searchViewModel.clearResultSearch()
                    adapter.notifyDataSetChanged()
                } else {
                    search(binding.searchField.text.toString())
                }
                if (binding.searchField.hasFocus() && s?.isEmpty() == true) searchViewModel.setShowMode(
                    SHOW_HISTORY
                ) else searchViewModel.setShowMode(NONE)
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
            if (hasFocus && binding.searchField.text.isEmpty()) searchViewModel.setShowMode(
                SHOW_HISTORY
            ) else searchViewModel.setShowMode(NONE)
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
        }
        historyViews.add(binding.clearHistoryButton)

        binding.internetNotAvailableButtonUpdate.setOnClickListener {
            if (binding.searchField.text.toString().isNotBlank()) {
                searchViewModel.findTrack(
                    binding.searchField.text.toString()
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
        }
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
        if(binding.searchField.text.isNullOrEmpty()) return
        binding.progressBar.isVisible = false
        setTopMargin(binding.trackRecycleView, R.dimen.dimen120dp)
        setHeightConstraint(R.dimen.dimen0dp)
        binding.trackRecycleView.isVisible = true
        this.emptySearchViews.forEach { it.isVisible = false }
        this.networkNotAvailableViews.forEach { it.isVisible = false }
        this.historyViews.forEach { it.isVisible = false }
        adapter.notifyDataSetChanged()
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
        showKeyboard()
        setTopMargin(binding.trackRecycleView, R.dimen.dimen172dp)
        setHeightConstraint(R.dimen.dimen400dp)
        binding.trackRecycleView.isVisible = true
        this.emptySearchViews.forEach { it.isVisible = false }
        this.networkNotAvailableViews.forEach { it.isVisible = false }
        this.historyViews.forEach { it.isVisible = true }
    }

    private fun showNone() {
        this.emptySearchViews.forEach { it.isVisible = false }
        this.networkNotAvailableViews.forEach { it.isVisible = false }
        this.historyViews.forEach { it.isVisible = false }
        binding.trackRecycleView.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun serverErrorMessage() {
        showNone()
        showToast(getString(R.string.bad_response))

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
        if(lastSearch.equals(searchString)) return
        lastSearch=searchString
        searchDebounce.searchTrack(searchString)
    }

}