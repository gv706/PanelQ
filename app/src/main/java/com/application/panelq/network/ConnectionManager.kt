package com.application.panelq.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class ConnectionManager{
    fun isNetworkAvailable(context: Context):Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if(Build.VERSION.SDK_INT >=23 ) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            networkCapabilities!= null && networkCapabilities
                .hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        }
        else
        {
           val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo!=null && networkInfo.isConnected && networkInfo.isAvailable && networkInfo.isConnectedOrConnecting
        }
    }
}