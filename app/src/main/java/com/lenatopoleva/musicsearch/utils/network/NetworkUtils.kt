package com.lenatopoleva.musicsearch.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.lang.RuntimeException


fun isOnline(context: Context): Boolean {
    var isOnline = false
    try {
        isOnline = getConnectionType(context) != 0
    } catch (ex: RuntimeException) {
        println("NetworkInfo exception: ${ex.message}")
    }
    return isOnline
}

fun getConnectionType(context: Context): Int {
    var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> result = 2
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> result = 1
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> result = 3
            }
        }
    } else {
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null) {
            // connected to the internet
            when (activeNetwork.type) {
                ConnectivityManager.TYPE_WIFI ->  result = 2
                ConnectivityManager.TYPE_MOBILE -> result = 1
                ConnectivityManager.TYPE_VPN -> result = 3
            }
        }
    }
    return result
}