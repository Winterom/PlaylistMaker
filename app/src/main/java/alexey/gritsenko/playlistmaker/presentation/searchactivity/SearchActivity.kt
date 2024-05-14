package alexey.gritsenko.playlistmaker.presentation.searchactivity

import alexey.gritsenko.playlistmaker.PlayListMakerApp
import alexey.gritsenko.playlistmaker.R
import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.R.layout
import alexey.gritsenko.playlistmaker.presentation.searchactivity.RequestStatus.CLEAR
import alexey.gritsenko.playlistmaker.presentation.searchactivity.RequestStatus.EMPTY
import alexey.gritsenko.playlistmaker.presentation.searchactivity.RequestStatus.NETWORK_ERROR
import alexey.gritsenko.playlistmaker.presentation.searchactivity.RequestStatus.OK
import alexey.gritsenko.playlistmaker.presentation.searchactivity.RequestStatus.SERVER_ERROR
import alexey.gritsenko.playlistmaker.presentation.searchactivity.ShowMode.SHOW_HISTORY
import alexey.gritsenko.playlistmaker.presentation.searchactivity.ShowMode.SHOW_SEARCH_RESULT
import alexey.gritsenko.playlistmaker.domain.SearchTrackInteractor
import alexey.gritsenko.playlistmaker.domain.TrackHistoryInteractor
import alexey.gritsenko.playlistmaker.domain.impl.SearchTrackInteractorDebounceWrapper
import alexey.gritsenko.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity(), TrackListChangedListener, HistoryListChangedListener {
    private val searchTrackInteractor: SearchTrackInteractor = SearchTrackInteractorDebounceWrapper()
    private lateinit var historyService: TrackHistoryInteractor
    private lateinit var recyclerView: RecyclerView
    private val emptySearchViews = ArrayList<View>()
    private val networkNotAvailableViews= ArrayList<View>()
    private val historyViews= ArrayList<View>()
    private lateinit var adapter: SearchTrackAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var searchField:EditText
    private lateinit var clearButton:ImageView
    private lateinit var updateNetNotAvailableButton:Button
    private val mConstrainLayout:ConstraintLayout by lazy {
        findViewById(id.search_layout)
    }

    companion object {
        const val TEXT_STORED_KEY = "searchText"
    }

    private var searchText: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search)
        searchTrackInteractor.addListener(this)
        historyService = TrackHistoryInteractorImpl(getSharedPreferences(PlayListMakerApp.APP_PREFERENCES,
            Context.MODE_PRIVATE))
        historyService.addListener(this)
        initProgressBar()
        initNetworkNotAvailableViews()
        initEmptySearchViews()
        initHistoryViews()
        initRecycleView()
        initReturnButton()
        initClearButton()
        initSearchField()
        initUpdateNetNotAvailableButton()
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
        this.adapter.notifyDataSetChanged()
    }
    override fun historyIsChanged() {
        if(adapter.getShowMode() == SHOW_HISTORY){
            this.adapter.notifyDataSetChanged()
        }
    }
    override fun onDestroy() {
        searchTrackInteractor.deleteListener(this)
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
    private fun initRecycleView(){
        recyclerView = findViewById(id.track_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val startPlayerActivityByDebounce = StartPlayerActivityByDebounce(this)
        this.adapter= SearchTrackAdapter(searchTrackInteractor, historyService,startPlayerActivityByDebounce)
        recyclerView.adapter = adapter
    }
    private fun initReturnButton(){
        val returnButton = findViewById<ImageView>(id.return_to_main)
        returnButton.setOnClickListener {
            finish()
        }
    }
    private fun initSearchField(){
        searchField = findViewById(id.searchField)
        searchField.requestFocus()
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val empty = searchField.text.isNullOrEmpty()
                clearButton.isVisible = !empty
                if (empty) {
                    closeKeyboard()
                    searchTrackInteractor.clearTracks()
                }else{
                    search(searchField.text.toString())
                }
                if(searchField.hasFocus()&&s?.isEmpty() == true)historyShow() else historyHide()
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
                    search(searchField.text.toString())
                }else{
                    showToast(getString(R.string.empty_search_field))
                }
            }
            true
        }
        searchField.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus && searchField.text.isEmpty())historyShow() else historyHide()
        }
    }
    private fun initClearButton(){
        clearButton = findViewById(id.clear_text)
        clearButton.setOnClickListener {
            searchField.setText("")
            searchTrackInteractor.findTrack("")
            closeKeyboard()
        }
    }
    private fun initUpdateNetNotAvailableButton(){
        updateNetNotAvailableButton = findViewById(id.internet_not_available_button_update)
        updateNetNotAvailableButton.setOnClickListener{
            if(searchField.text.toString().isNotBlank()){
                searchTrackInteractor.findTrack(searchField.text.toString())
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

    private fun initHistoryViews(){
        historyViews.add(findViewById(id.you_history_text))
        val clearHistoryButton = findViewById<Button>(id.clear_history_button)
        clearHistoryButton.setOnClickListener {
            historyService.clearHistory()
            adapter.notifyDataSetChanged()
            historyHide()
        }
        historyViews.add(clearHistoryButton)

    }

    private fun initProgressBar(){
        this.progressBar = findViewById(id.progressBar)
    }

    private fun emptySearchResult(){
        this.progressBar.isVisible=false
        showKeyboard()
        this.recyclerView.isVisible = false
        this.emptySearchViews.forEach{it.isVisible = true}
        this.networkNotAvailableViews.forEach{it.isVisible = false}
        this.historyViews.forEach { it.isVisible=false }
    }
    private fun okSearchResult(){
        this.progressBar.isVisible=false
        this.adapter.setShowMode(SHOW_SEARCH_RESULT)
        setTopMargin(this.recyclerView,R.dimen.dimen120dp)
        setHeightConstraint(R.dimen.dimen0dp)
        this.recyclerView.marginTop
        this.recyclerView.isVisible = true
        this.emptySearchViews.forEach{it.isVisible = false}
        this.networkNotAvailableViews.forEach{it.isVisible = false}
        this.historyViews.forEach { it.isVisible=false }
    }
    private fun networkNotAvailable(){
        this.progressBar.isVisible=false
        closeKeyboard()
        this.recyclerView.isVisible = false
        this.emptySearchViews.forEach{it.isVisible = false}
        this.networkNotAvailableViews.forEach{it.isVisible = true}
        this.historyViews.forEach { it.isVisible=false }
    }

    private fun historyShow(){
        if(historyService.getCount()==0) return
        showKeyboard()
        this.adapter.setShowMode(SHOW_HISTORY)
        setTopMargin(this.recyclerView,R.dimen.dimen172dp)
        setHeightConstraint(R.dimen.dimen240dp)
        this.recyclerView.isVisible = true
        this.emptySearchViews.forEach{it.isVisible = false}
        this.networkNotAvailableViews.forEach{it.isVisible = false}
        this.historyViews.forEach { it.isVisible=true }
    }
    private fun historyHide(){
        this.historyViews.forEach { it.isVisible=false }
        this.recyclerView.isVisible = false
    }
    private fun serverErrorMessage(){
        showToast("Что то пошло не так!")
        emptySearchResult()
    }

    private fun setTopMargin(view: View, @DimenRes id:Int){
        val layoutParams=view.layoutParams as MarginLayoutParams
        val dp= resources.getDimensionPixelSize(id)
        layoutParams.setMargins(0,dp,0,0)
    }

    private fun setHeightConstraint(@DimenRes height:Int){
        val constraint = ConstraintSet()
        constraint.clone(mConstrainLayout)
        val dp= resources.getDimensionPixelSize(height)
        constraint.constrainMaxHeight(id.track_recycle_view,dp)
        constraint.applyTo(mConstrainLayout)
    }
    private fun search(searchString: String){
        this.progressBar.isVisible=true
        searchTrackInteractor.findTrack(searchString)
    }

}