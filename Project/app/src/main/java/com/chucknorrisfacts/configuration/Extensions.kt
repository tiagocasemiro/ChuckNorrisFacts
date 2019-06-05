package com.chucknorrisfacts.configuration

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.domain.Searched

fun Activity.hideKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
}

fun hideSoftKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager!!.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
}

@Suppress("DEPRECATION")
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

fun MutableList<Searched>.addOverridingIfExists(searched: Searched) {
    if(this.contains(searched)) {
        this.remove(searched)
    }
    this.add(searched)
}