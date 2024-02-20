package alexey.gritsenko.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val returnButton: ImageView = findViewById(R.id.return_to_main)
        returnButton.setOnClickListener {
            finish()
        }
        val shareButton: ImageView = findViewById(R.id.share_button)
        shareButton.setOnClickListener {
            val message = getString(R.string.share_app_content)
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent,"Share"))

        }
        val supportButton: ImageView = findViewById(R.id.support_button)
        supportButton.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                val message = getString(R.string.settings_support_message)
                val subject = getString(R.string.settings_support_subject)
                val mail = getString(R.string.settings_support_mail)
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
                startActivity(this)
            }

        }
        val offerButton: ImageView = findViewById(R.id.offer_button)
        offerButton.setOnClickListener {
            val uri = resources.getString(R.string.yandex_offer)
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            )
            startActivity(urlIntent)
        }
    }
}

