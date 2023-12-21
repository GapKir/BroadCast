package com.example.broadcast.utils


object NetworkStatus {

    private var isNetworkConnected = false
    private val networkListeners = mutableListOf<NetworkListener>()

    enum class Statuses{
        NETWORK_CONNECTED, NETWORK_DISCONNECTED
    }

    fun changeNetworkStatus(isNetworkActive: Boolean){
        isNetworkConnected = isNetworkActive
        networkListeners.forEach{
            it.networkChanged(getNetworkStatus())
        }
        Logger.log("isNetworkConnected = $isNetworkConnected")

    }

    fun addListener(listener: NetworkListener){
        networkListeners.add(listener)
        listener.networkChanged(getNetworkStatus())
    }

    fun removeListeners(){
        networkListeners.clear()
    }

    private fun getNetworkStatus(): Statuses{
        return if (isNetworkConnected) {
            Statuses.NETWORK_CONNECTED
        } else {
            Statuses.NETWORK_DISCONNECTED
        }
    }

}