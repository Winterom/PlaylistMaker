package alexey.gritsenko.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    private var searchText: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val searchField: EditText = findViewById(R.id.searchField)
        val clearButton =findViewById<ImageView>(R.id.clear_text)
            clearButton.setOnClickListener {
                searchField.setText("")
            }

            val simpleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // empty
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // empty
                    clearButton.visibility = clearButtonVisibility(s)
                }

                override fun afterTextChanged(s: Editable?) {
                    // empty
                }
            }
            searchField.addTextChangedListener(simpleTextWatcher)
            searchField.setText(searchText)
        }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("searchText", searchText)
        super.onSaveInstanceState(outState)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString("searchText") ?: ""
    }
        private fun clearButtonVisibility(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

}