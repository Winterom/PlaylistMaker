package alexey.gritsenko.playlistmaker.domain.sharing.impl


import alexey.gritsenko.playlistmaker.domain.sharing.AppNavigator
import alexey.gritsenko.playlistmaker.domain.sharing.SharingInteractor

class SharingInteractorImpl( private var appNavigator: AppNavigator): SharingInteractor {
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