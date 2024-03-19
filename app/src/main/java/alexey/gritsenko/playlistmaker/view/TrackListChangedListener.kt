package alexey.gritsenko.playlistmaker.view

interface TrackListChangedListener {
    fun dataIsChanged(status: RequestStatus)
}
interface HistoryListChangedListener {
    fun historyIsChanged()
}
enum class RequestStatus{
    OK, EMPTY, CLEAR, NETWORK_ERROR, SERVER_ERROR
}