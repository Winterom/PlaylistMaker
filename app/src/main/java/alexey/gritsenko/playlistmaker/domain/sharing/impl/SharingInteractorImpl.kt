package alexey.gritsenko.playlistmaker.domain.sharing.impl

import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.sharing.SharingInteractor
import alexey.gritsenko.playlistmaker.platform.sharing.ExternalNavigator

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