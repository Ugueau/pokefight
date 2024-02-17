package com.example.pokefight.domain.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ConnectionManager {
    companion object {
        fun checkNetworkState(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // Check WiFi connection
            val wifiNetwork = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            val isWifiConnected = wifiNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true

            // Check mobile data connection
            val cellularNetwork = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            val isMobileConnected = cellularNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true

            return isMobileConnected || isWifiConnected
        }
    }
}