package com.chucknorrisfacts.configuration

import android.content.Context
import android.net.ConnectivityManager

class Connection {
    @Suppress("DEPRECATION")
    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting
    }
}