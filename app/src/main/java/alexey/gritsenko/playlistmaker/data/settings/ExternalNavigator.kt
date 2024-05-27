package alexey.gritsenko.playlistmaker.data.settings

import alexey.gritsenko.playlistmaker.data.settings.model.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: EmailData)
}