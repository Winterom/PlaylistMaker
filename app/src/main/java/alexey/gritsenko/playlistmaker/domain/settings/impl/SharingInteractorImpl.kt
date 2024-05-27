package alexey.gritsenko.playlistmaker.domain.settings.impl

import alexey.gritsenko.playlistmaker.data.settings.ExternalNavigator
import alexey.gritsenko.playlistmaker.data.settings.model.EmailData
import alexey.gritsenko.playlistmaker.domain.settings.SharingInteractor

class SharingInteractorImpl(

):SharingInteractor {
    private lateinit var externalNavigator: ExternalNavigator
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }
    private fun getShareAppLink(): String {
       TODO()
    }

    private fun getSupportEmailData(): EmailData {
        TODO()
    }

    private fun getTermsLink(): String {
        TODO()
    }
}