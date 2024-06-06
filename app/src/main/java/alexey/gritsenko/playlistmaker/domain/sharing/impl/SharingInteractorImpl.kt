package alexey.gritsenko.playlistmaker.domain.sharing.impl

import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.sharing.AppNavigator
import alexey.gritsenko.playlistmaker.domain.sharing.SharingInteractor

class SharingInteractorImpl: SharingInteractor {
    private var appNavigator: AppNavigator =
        ServiceLocator.getService(AppNavigator::class.java)
    override fun shareApp() {
        appNavigator.shareLink()
    }

    override fun openTerms() {
         appNavigator.openLink()
    }

    override fun openSupport() {
          appNavigator.openEmail()
    }

}