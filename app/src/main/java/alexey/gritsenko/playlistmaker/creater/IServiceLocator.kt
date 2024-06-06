package alexey.gritsenko.playlistmaker.creater



interface IServiceLocator {
    fun <T>getService(clazz:Class<T>):T
}