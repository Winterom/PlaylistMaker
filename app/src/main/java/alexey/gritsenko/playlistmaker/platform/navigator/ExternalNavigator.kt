package alexey.gritsenko.playlistmaker.platform.navigator

import alexey.gritsenko.playlistmaker.domain.search.entity.Track


interface ExternalNavigator {
    fun shareLink()
    fun openLink()
    fun openEmail()
    fun startPlayerActivity(track: Track)
}