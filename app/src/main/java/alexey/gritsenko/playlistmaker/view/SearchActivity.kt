package alexey.gritsenko.playlistmaker.view

import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.R.layout
import alexey.gritsenko.playlistmaker.services.SearchTrackViewModel
import alexey.gritsenko.playlistmaker.services.impl.SearchTrackViewModelImpl
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity(),DataChangedObserver {
    private val searchTrackViewModel: SearchTrackViewModel = SearchTrackViewModelImpl()
    private lateinit var recyclerView: RecyclerView
    companion object {
        const val TEXT_STORED_KEY = "searchText"
    }

    private var searchText: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search)
        searchTrackViewModel.addListener(this)
        val returnButton = findViewById<ImageView>(id.return_to_main)
        val searchField = findViewById<EditText>(id.searchField)
        val clearButton = findViewById<ImageView>(id.clear_text)
        recyclerView = findViewById(id.track_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SearchTrackAdapter(searchTrackViewModel)
        returnButton.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            searchField.setText("")
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val empty = s.isNullOrEmpty()
                clearButton.isVisible = !empty
                if (empty) closeKeyboard()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        searchField.addTextChangedListener(simpleTextWatcher)
        searchField.setText(searchText)
        searchField.setOnEditorActionListener{_, actionId, _ ->
            if(actionId== EditorInfo.IME_ACTION_DONE){
                if(searchField.text.toString().isNotBlank()){
                    searchTrackViewModel.findTrack(searchField.text.toString())
                }else{

                }
            }
            true
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

    override fun dataIsChanged() {
        this.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        searchTrackViewModel.deleteListener(this)
        super.onDestroy()
    }
}