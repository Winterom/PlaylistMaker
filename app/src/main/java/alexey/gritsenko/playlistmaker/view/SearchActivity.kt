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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity(),TrackListChangedListener {
    private val searchTrackService: SearchTrackService = SearchTrackServiceImpl
    private lateinit var recyclerView: RecyclerView
    private val emptySearchViews = ArrayList<View>()
    private val networkNotAvailableViews= ArrayList<View>()
    private lateinit var searchField:EditText
    private lateinit var clearButton:ImageView
    private lateinit var updateButton:Button
    companion object {
        const val TEXT_STORED_KEY = "searchText"
    }

    private var searchText: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search)
        searchTrackService.addListener(this)
        initNetworkNotAvailableViews()
        initEmptySearchViews()
        initRecycleView()
        initReturnButton()
        initClearButton()
        initSearchField()
        initUpdateButton()
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
    private fun showKeyboard(){
        if (searchField.requestFocus()) {
            val inputManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT)
        }
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
        showKeyboard()
        this.recyclerView.isVisible = false
        this.emptySearchViews.forEach{it.isVisible = true}
        this.networkNotAvailableViews.forEach{it.isVisible = false}
    }
    private fun okSearchResult(){
        showKeyboard()
        this.recyclerView.isVisible = true
        this.emptySearchViews.forEach{it.isVisible = false}
        this.networkNotAvailableViews.forEach{it.isVisible = false}
    }
    private fun networkNotAvailable(){
        closeKeyboard()
        this.recyclerView.isVisible = false
        this.emptySearchViews.forEach{it.isVisible = false}
        this.networkNotAvailableViews.forEach{it.isVisible = true}
    }
    private fun serverErrorMessage(){
        showToast("Что то пошло не так!")
        emptySearchResult()
    }
    private fun initRecycleView(){
        recyclerView = findViewById(id.track_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SearchTrackAdapter(searchTrackService)
    }
    private fun initReturnButton(){
        val returnButton = findViewById<ImageView>(id.return_to_main)
        returnButton.setOnClickListener {
            finish()
        }
    }
    private fun initSearchField(){
        searchField = findViewById(id.searchField)
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
                    closeKeyboard()
                    searchTrackService.findTrack(searchField.text.toString())
                }else{
                    showToast(getString(R.string.empty_search_field))
                }
            }
            true
        }
    }
    private fun initClearButton(){
        clearButton = findViewById(id.clear_text)
        clearButton.setOnClickListener {
            searchField.setText("")
            closeKeyboard()
        }
    }
    private fun initUpdateButton(){
        updateButton = findViewById(id.internet_not_available_button_update)
        updateButton.setOnClickListener{
            if(searchField.text.toString().isNotBlank()){
                searchTrackService.findTrack(searchField.text.toString())
            }else{
                showToast(getString(R.string.empty_search_field))
            }
        }
    }

    private fun initEmptySearchViews(){
        emptySearchViews.add(findViewById(id.empty_search_image))
        emptySearchViews.add(findViewById(id.empty_search_text))
    }

    private fun initNetworkNotAvailableViews(){
        networkNotAvailableViews.add(findViewById(id.internet_not_available_button_update))
        networkNotAvailableViews.add(findViewById(id.internet_not_available_image))
        networkNotAvailableViews.add(findViewById(id.internet_not_available_text))
    }
}