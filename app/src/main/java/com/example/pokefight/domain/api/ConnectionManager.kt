package com.example.pokefight.domain.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
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
            if (Build.DEVICE.contains("emulator")){
                return true
            }

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

        private fun isInternetReachable(): Flow<Boolean> = flow {
            try {
                // Use a lightweight HTTP HEAD request instead of ICMP ping
                val url = java.net.URL("https://clients3.google.com/generate_204")
                val connection = url.openConnection() as java.net.HttpURLConnection
                connection.connectTimeout = 3000
                connection.readTimeout = 3000
                connection.requestMethod = "HEAD"
                connection.instanceFollowRedirects = false
                connection.connect()
                val success = connection.responseCode == 204
                Timber.tag("ConnectionManager").e("HTTP success: $success (code ${connection.responseCode})")
                emit(success)
                connection.disconnect()
            } catch (e: Exception) {
                Timber.tag("ConnectionManager").e("HTTP check failed: ${e.message}")
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }
}