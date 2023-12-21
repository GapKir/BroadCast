package com.example.broadcast.utils

interface NetworkListener {
    fun networkChanged(networkStatus: NetworkStatus.Statuses)
}