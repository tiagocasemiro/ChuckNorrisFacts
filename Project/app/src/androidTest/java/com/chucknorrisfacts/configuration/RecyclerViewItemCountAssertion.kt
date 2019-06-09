package com.chucknorrisfacts.configuration

import android.view.View
import androidx.test.espresso.ViewAssertion
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`


class RecyclerViewItemCountAssertion(expectedCount: Int) : ViewAssertion {
    private val matcher: Matcher<Int> = `is`(expectedCount)

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat(adapter!!.itemCount, matcher)
    }
}