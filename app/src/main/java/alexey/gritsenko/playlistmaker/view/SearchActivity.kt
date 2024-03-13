package alexey.gritsenko.playlistmaker.view

import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.R.layout
import alexey.gritsenko.playlistmaker.services.SearchTrackService
import alexey.gritsenko.playlistmaker.services.impl.SearchTrackServiceImpl
import alexey.gritsenko.playlistmaker.view.RequestStatus.CLEAR
import alexey.gritsenko.playlistmaker.view.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.view.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.view.RequestStatus.OK
import alexey.gritsenko.playlistmaker.view.RequestStatus.SERVER_ERROR
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity(),TrackListChangedListener {
    private val searchTrackService: SearchTrackService = SearchTrackServiceImpl()
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptySearchLayout:LinearLayout
    private lateinit var networkNotAvailableLayout:LinearLayout
    companion object {
        const val TEXT_STORED_KEY = "searchText"
    }

    private var searchText: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search)
        searchTrackService.addListener(this)
        recyclerView = findViewById(id.track_recycle_view)
        networkNotAvailableLayout = findViewById(id.network_not_available)
        emptySearchLayout = findViewById(id.empty_search_layout)
        val returnButton = findViewById<ImageView>(id.return_to_main)
        val searchField = findViewById<EditText>(id.searchField)
        val clearButton = findViewById<ImageView>(id.clear_text)
        val updateButton = findViewById<Button>(id.button_update)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SearchTrackAdapter(searchTrackService)
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
                if (empty) {
                    closeKeyboard()
                    searchTrackService.clearTracks()
                }
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
                    searchTrackService.findTrack(searchField.text.toString())
                }else{
                    showToast(getString(R.string.empty_search_field))
                }
            }
            true
        }
        updateButton.setOnClickListener{
            if(searchField.text.toString().isNotBlank()){
                searchTrackService.findTrack(searchField.text.toString())
            }else{
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

    override fun dataIsChanged(status: RequestStatus) {
            when(status){
                OK,CLEAR -> okSearchResult()
                EMPTY -> emptySearchResult()
                NETWORK_ERROR -> networkNotAvailable()
                SERVER_ERROR -> serverErrorMessage()
            }
        this.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        searchTrackService.deleteListener(this)
        super.onDestroy()
    }

    private fun showToast(text:String){
        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
        toast.setGravity(
            Gravity.TOP or Gravity.CENTER,
            0,
            0
        )
        toast.show()
    }

    private fun emptySearchResult(){
        this.recyclerView.visibility = View.GONE
        this.emptySearchLayout.visibility = View.VISIBLE
        this.networkNotAvailableLayout.visibility = View.GONE
    }

    private fun okSearchResult(){
        this.recyclerView.visibility = View.VISIBLE
        this.emptySearchLayout.visibility = View.GONE
        this.networkNotAvailableLayout.visibility = View.GONE
    }

    private fun networkNotAvailable(){
        this.recyclerView.visibility = View.GONE
        this.emptySearchLayout.visibility = View.GONE
        this.networkNotAvailableLayout.visibility = View.VISIBLE
    }

    private fun serverErrorMessage(){
        showToast("Что то пошло не так!")
        emptySearchResult()
    }


}