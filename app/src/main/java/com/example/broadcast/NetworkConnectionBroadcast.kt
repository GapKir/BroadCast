package com.example.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.broadcast.utils.Logger
import com.example.broadcast.utils.NetworkStatus

class NetworkConnectionBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        checkNetworkConnection(context)
    }

    private fun checkNetworkConnection(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        val isNetworkActive = networkCapabilities?.isNetworkCapabilitiesValid() == true

        NetworkStatus.changeNetworkStatus(isNetworkActive)

        if (isNetworkActive) {
            Logger.log(NetworkStatus.Statuses.NETWORK_CONNECTED.name  + " from Broadcast")

        } else {
            Logger.log(NetworkStatus.Statuses.NETWORK_DISCONNECTED.name  + " from Broadcast")
        }

    }

    private fun NetworkCapabilities?.isNetworkCapabilitiesValid(): Boolean = when {
        this == null -> false
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) -> true

        else -> false
    }

}