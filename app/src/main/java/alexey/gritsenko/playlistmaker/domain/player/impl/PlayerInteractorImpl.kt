package alexey.gritsenko.playlistmaker.domain.player.impl

import alexey.gritsenko.playlistmaker.creater.ServiceLocator
import alexey.gritsenko.playlistmaker.domain.player.PlayerInteractor
import alexey.gritsenko.playlistmaker.domain.player.StatusObserver
import alexey.gritsenko.playlistmaker.platform.player.TrackPlayer

class PlayerInteractorImpl:PlayerInteractor {
    private val trackPlayer=ServiceLocator.getService(TrackPlayer::class.java)
    override fun prepare(previewUrl: String, statusObserver: StatusObserver) {
        trackPlayer.prepare(previewUrl,statusObserver)
    }

    override fun play() {
        trackPlayer.play()
    }

    override fun pause() {
        trackPlayer.pause()
    }

    override fun release() {
        trackPlayer.release()
    }
}