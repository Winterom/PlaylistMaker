package alexey.gritsenko.playlistmaker.data.navigator

import alexey.gritsenko.playlistmaker.domain.search.entity.Track


interface ExternalNavigator {
    fun shareLink()
    fun openLink()
    fun openEmail()
    fun startPlayerActivity(track: Track)
}