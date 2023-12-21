package com.example.broadcast.utils

import android.util.Log

object Logger {
    private const val TAG = "NetworkConnection"

    fun log(text: String){
        Log.d(TAG, text)
    }
}