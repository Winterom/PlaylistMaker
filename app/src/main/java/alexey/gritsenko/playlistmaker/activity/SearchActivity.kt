package alexey.gritsenko.playlistmaker.activity

import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.R.layout
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class SearchActivity : AppCompatActivity() {
    companion object{
       const val TEXT_STORED_KEY="searchText"
    }
    private var searchText: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search)
        val returnButton: ImageView = findViewById(id.return_to_main)
        val searchField: EditText = findViewById(id.searchField)
        val clearButton = findViewById<ImageView>(id.clear_text)
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
                if(empty) closeKeyboard()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        searchField.addTextChangedListener(simpleTextWatcher)
        searchField.setText(searchText)
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
}