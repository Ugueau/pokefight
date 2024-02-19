package com.example.pokefight.domain.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.InetAddress

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

        suspend fun checkInternetConnection(context: Context): Boolean{
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)

            // Check if the device is connected to the internet (can reach a specific server)
            if (capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
                var success = false
                isInternetReachable().collect{
                    success = it
                }
                return success
            }
            return false
        }

        suspend private fun isInternetReachable():  Flow<Boolean> = flow {
            try {
                // Use a reliable server, such as Google's DNS server (8.8.8.8), for the connectivity check
                val address = InetAddress.getByName("8.8.8.8")
                val success = address.isReachable(3000)  // 3 seconds timeout
                emit(success)
            } catch (e: Exception) {
                Log.e("Connection Manager", e.message.toString())
                emit(false)
            }
        }
            .flowOn(Dispatchers.IO)
    }
}