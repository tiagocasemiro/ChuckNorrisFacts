package com.chucknorrisfacts.view

import android.app.Activity
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewGroup
import com.chucknorrisfacts.R
import kotlinx.android.synthetic.main.component_load.view.*

class Load constructor(activity: Activity, color: Color = Color.PRIMARY_COLOR) {
    private val load: View?

    init {
        val inflater = activity.layoutInflater
        val window = activity.window

        load = inflater.inflate(R.layout.component_load, activity.window.decorView as ViewGroup, false)
        load!!.visibility = View.INVISIBLE
        load.setOnClickListener { Snackbar.make(load, R.string.aguarde, Snackbar.LENGTH_SHORT).show() }

        if (color == Color.DARK) {
            load.progressBarCircularIndeterminate.setBackgroundResource(R.drawable.round_load)
        }

        window.addContentView(load, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    fun show() {
        if (load != null)
            load.visibility = View.VISIBLE
    }

    fun hide() {
        if (load != null)
            load.visibility = View.GONE
    }

    enum class Color {
        DARK, PRIMARY_COLOR
    }
}