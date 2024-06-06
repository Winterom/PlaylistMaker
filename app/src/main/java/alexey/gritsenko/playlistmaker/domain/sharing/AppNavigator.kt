package alexey.gritsenko.playlistmaker.domain.sharing

import alexey.gritsenko.playlistmaker.domain.search.entity.Track


interface AppNavigator {
    fun shareLink()
    fun openLink()
    fun openEmail()
    fun startPlayerActivity(track: Track)
}