package com.example.broadcast

import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.broadcast.databinding.ActivityMainBinding
import com.example.broadcast.utils.Logger
import com.example.broadcast.utils.NetworkListener
import com.example.broadcast.utils.NetworkStatus

class MainActivity : AppCompatActivity(), NetworkListener {
    private lateinit var binding: ActivityMainBinding
    private val networkConnectionBroadcast = NetworkConnectionBroadcast()
    private lateinit var listOfImageView: List<ImageView>
    private var hasReceiver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            listOfImageView = listOf(image1, image2, image3, image4, image5)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            registerNetworkCallback()
        } else {
            registerBroadcast()
        }
        addNetworkListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        NetworkStatus.removeListeners()
        if (hasReceiver) {
            unregisterReceiver(networkConnectionBroadcast)
            Logger.log(UNREGISTER)
        }
    }

    override fun networkChanged(networkStatus: NetworkStatus.Statuses) {
        if (networkStatus == NetworkStatus.Statuses.NETWORK_CONNECTED) {
            runOnUiThread { loadImages() }

        } else {
            waitNetwork()
        }
    }

    private fun registerNetworkCallback() {
        NetworkConnectionCallback(this)
    }

    private fun registerBroadcast() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkConnectionBroadcast, filter)
        hasReceiver = true
        Logger.log(REGISTER)
    }

    private fun addNetworkListener() {
        NetworkStatus.addListener(this)
    }

    private fun loadImages() {
        listOfImageView.forEach {
            it.visibility = View.VISIBLE
        }
        binding.root.setBackgroundColor(Color.BLACK)
        Glide.with(this).apply {
            for ((index, image) in listOfImageView.withIndex()) {
                this
                    .load(imageList[index])
                    .circleCrop()
                    .into(image)
            }
        }
    }

    private fun waitNetwork() {
        listOfImageView.forEach {
            it.visibility = View.INVISIBLE
        }
        binding.root.setBackgroundColor(Color.RED)
        Toast.makeText(this, NETWORK_LOST, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REGISTER = "Register BroadcastReceiver"
        private const val UNREGISTER = "Unregister BroadcastReceiver"
        private const val NETWORK_LOST = "Network lost"

        private val imageList = listOf(
            "https://images.pexels.com/photos/268533/pexels-photo-268533.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
            "https://img.freepik.com/free-photo/painting-mountain-lake-with-mountain-background_188544-9126.jpg",
            "https://buffer.com/library/content/images/size/w1200/2023/10/free-images.jpg",
            "https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8aHVtYW58ZW58MHx8MHx8fDA%3D",
        ).shuffled()
    }
}