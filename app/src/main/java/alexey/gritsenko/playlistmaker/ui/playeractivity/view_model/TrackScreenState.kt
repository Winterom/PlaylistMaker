package alexey.gritsenko.playlistmaker.ui.playeractivity.view_model

import java.util.Locale

class TrackScreenState {
    var currentPosition:Int=0 //in millis


    fun getPosition():String{
        var seconds = currentPosition/ 1000
        val minutes = seconds / 60
        seconds %= 60
        return String.format(Locale("ru"),"%d:%02d", minutes, seconds)
    }
}
