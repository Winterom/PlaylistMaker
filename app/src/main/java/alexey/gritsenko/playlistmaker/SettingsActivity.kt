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

        val shareButton: ImageView = findViewById(R.id.share_button)
        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, R.string.share_app_content)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        val supportButton: ImageView = findViewById(R.id.support_button)
        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("winterov@yandex.ru"))
            startActivity(supportIntent)
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

