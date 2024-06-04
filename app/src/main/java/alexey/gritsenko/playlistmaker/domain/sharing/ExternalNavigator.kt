package alexey.gritsenko.playlistmaker.domain.sharing

import alexey.gritsenko.playlistmaker.domain.search.entity.Track


interface ExternalNavigator {
    fun shareLink()
    fun openLink()
    fun openEmail()
    fun startPlayerActivity(track: Track)
}