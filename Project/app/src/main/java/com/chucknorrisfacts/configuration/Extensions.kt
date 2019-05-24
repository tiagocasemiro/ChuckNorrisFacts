package com.chucknorrisfacts.configuration

import android.app.Activity

import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun Fragment.isConnected(noConnection : () -> Unit) {
    activity?.let {
        val cm = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(!(cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting)) {
            noConnection()
        }
    }?: run {
            noConnection()
    }
}