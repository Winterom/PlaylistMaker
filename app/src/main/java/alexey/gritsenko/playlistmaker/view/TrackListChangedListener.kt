package alexey.gritsenko.playlistmaker.view

interface TrackListChangedListener {
    fun dataIsChanged(status: RequestStatus)
}

enum class RequestStatus{
    OK, EMPTY, NETWORK_ERROR, SERVER_ERROR
}