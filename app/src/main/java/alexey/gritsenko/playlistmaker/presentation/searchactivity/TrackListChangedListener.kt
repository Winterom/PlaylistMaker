package alexey.gritsenko.playlistmaker.presentation.searchactivity

interface TrackListChangedListener {
    fun dataIsChanged(status: RequestStatus)
}
interface HistoryListChangedListener {
    fun historyIsChanged()
}
enum class RequestStatus{
    OK, EMPTY, CLEAR, NETWORK_ERROR, SERVER_ERROR
}