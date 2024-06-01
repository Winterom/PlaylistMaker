package alexey.gritsenko.playlistmaker.data.navigator.impl



import alexey.gritsenko.playlistmaker.R.string
import alexey.gritsenko.playlistmaker.domain.search.entity.Track
import alexey.gritsenko.playlistmaker.data.navigator.ExternalNavigator
import alexey.gritsenko.playlistmaker.ui.playeractivity.activity.PlayerActivity
import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Handler
import android.os.Looper

class ExternalNavigatorImpl(private val application: Application): ExternalNavigator {
    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    override fun shareLink() {
        val message = application.resources.getString(string.share_app_content)
        val intent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        },"Share")
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        application.applicationContext.startActivity(intent)
    }

    override fun openLink(){
        val uri = application.resources.getString(string.yandex_offer)
        val intent=Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        application.applicationContext.startActivity(intent)
    }

    override fun openEmail() {
       val intent=Intent(Intent.ACTION_SENDTO).apply {
            val message = application.resources.getString(string.settings_support_message)
            val subject = application.resources.getString(string.settings_support_subject)
            val mail = application.resources.getString(string.settings_support_mail)
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        application.applicationContext.startActivity(intent)
    }

    override fun startPlayerActivity(track: Track) {
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }else{
            return
        }
        val intent = Intent(application.applicationContext, PlayerActivity::class.java)
        intent.putExtra(PlayerActivity.TRACK,track)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        application.applicationContext.startActivity(intent)
    }


}