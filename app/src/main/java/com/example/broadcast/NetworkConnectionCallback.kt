package com.example.broadcast

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.example.broadcast.utils.Logger
import com.example.broadcast.utils.NetworkStatus

/** Google recommended it https://developer.android.com/training/monitoring-device-state/connectivity-status-type */
class NetworkConnectionCallback(
    context: Context
) {

    init {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network : Network) {
                NetworkStatus.changeNetworkStatus(true)
                Logger.log(NetworkStatus.Statuses.NETWORK_CONNECTED.name + " from Callback")
            }

            override fun onLost(network : Network) {
                NetworkStatus.changeNetworkStatus(false)
                Logger.log(NetworkStatus.Statuses.NETWORK_DISCONNECTED.name + " from Callback")
            }
        })
    }
}