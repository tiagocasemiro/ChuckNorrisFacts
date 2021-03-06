package com.chucknorrisfacts.configuration

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.domain.Category
import com.domain.Searched

fun Activity.hideKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(window.decorView.windowToken, 0)
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

fun List<Category>.shuffledAndSlice() : List<Category> {
    var list = this.shuffled()
    if (list.size > 8)
        list = list.slice(0..7)

    return list
}

fun String.diferent(other: String) : Boolean {
    return !this.equals(other)
}