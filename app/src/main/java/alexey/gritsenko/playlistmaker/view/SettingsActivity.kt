package alexey.gritsenko.playlistmaker.view

import alexey.gritsenko.playlistmaker.R.id
import alexey.gritsenko.playlistmaker.R.layout
import alexey.gritsenko.playlistmaker.R.string
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_settings)
        val returnButton: ImageView = findViewById(id.return_to_main)
        returnButton.setOnClickListener {
            finish()
        }
        val shareButton: ImageView = findViewById(id.share_button)
        shareButton.setOnClickListener {
            val message = getString(string.share_app_content)
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
                startActivity(Intent.createChooser(this,"Share"))
            }
        }
        val supportButton: ImageView = findViewById(id.support_button)
        supportButton.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                val message = getString(string.settings_support_message)
                val subject = getString(string.settings_support_subject)
                val mail = getString(string.settings_support_mail)
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
                startActivity(this)
            }

        }
        val offerButton: ImageView = findViewById(id.offer_button)
        offerButton.setOnClickListener {
            val uri = resources.getString(string.yandex_offer)
            Intent(Intent.ACTION_VIEW,Uri.parse(uri)).apply {
                    startActivity(this)
            }

        }
    }
}

