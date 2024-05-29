package alexey.gritsenko.playlistmaker.platform.sharing



interface ExternalNavigator {
    fun shareLink()
    fun openLink()
    fun openEmail()
}