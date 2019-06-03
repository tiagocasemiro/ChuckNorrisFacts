package com.chucknorrisfacts.configuration

import android.app.Activity

import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun Activity.hideKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun Fragment.isConnected(noConnection : () -> Unit) : Boolean{
    activity?.let {
        val cm = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(!(cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting)) {
            noConnection()
            return false
        }
        return true
    }?: run {
        noConnection()
        return false
    }
}

fun Fragment.hideBackNavegation() {
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
}