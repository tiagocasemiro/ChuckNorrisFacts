package com.chucknorrisfacts.configuration

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import io.github.inflationx.calligraphy3.TypefaceUtils

fun Activity.hideKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun TextView.setFont(font: String) {
    typeface = TypefaceUtils.load(context?.assets, "fonts/$font")
}