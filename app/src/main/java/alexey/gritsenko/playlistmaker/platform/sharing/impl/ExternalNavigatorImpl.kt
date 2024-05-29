package alexey.gritsenko.playlistmaker.platform.sharing.impl



import alexey.gritsenko.playlistmaker.R.string
import alexey.gritsenko.playlistmaker.platform.sharing.ExternalNavigator
import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri

class ExternalNavigatorImpl(private val application: Application): ExternalNavigator {
    override fun shareLink() {
        val message = application.resources.getString(string.share_app_content)
        val intent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            type = "text/plain"
        },"Share")
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
       application.baseContext.startActivity(intent)
    }

    override fun openLink(){
        val uri = application.resources.getString(string.yandex_offer)
        val intent=Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        application.baseContext.startActivity(intent)
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
        application.startActivity(intent)
    }
}