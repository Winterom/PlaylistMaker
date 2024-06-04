package alexey.gritsenko.playlistmaker.domain.sharing.impl

import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.sharing.ExternalNavigator
import alexey.gritsenko.playlistmaker.domain.sharing.SharingInteractor

class SharingInteractorImpl: SharingInteractor {
    private var externalNavigator: ExternalNavigator =
        ServiceLocator.getService(ExternalNavigator::class.java)
    override fun shareApp() {
        externalNavigator.shareLink()
    }

    override fun openTerms() {
         externalNavigator.openLink()
    }

    override fun openSupport() {
          externalNavigator.openEmail()
    }

}