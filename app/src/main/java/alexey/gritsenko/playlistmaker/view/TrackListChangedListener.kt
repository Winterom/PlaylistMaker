package alexey.gritsenko.playlistmaker.view

interface TrackListChangedListener {
    fun dataIsChanged(status: RequestStatus)
}

enum class RequestStatus{
    OK, EMPTY, CLEAR, NETWORK_ERROR, SERVER_ERROR
}