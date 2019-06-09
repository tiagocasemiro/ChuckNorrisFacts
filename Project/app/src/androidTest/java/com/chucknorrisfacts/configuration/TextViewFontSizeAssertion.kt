package com.chucknorrisfacts.configuration

import android.view.View
import android.widget.TextView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.`is`


class TextViewFontSizeAssertion : ViewAssertion {

    private val limitCharacter = 80
    private val fontSizeBig = 40.0f
    private val fontSizeSmall = 32.0f

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val textView = view as TextView
        if (textView.text.length > limitCharacter) {
            MatcherAssert.assertThat(textView.textSize, `is`(fontSizeSmall))
        } else {
            MatcherAssert.assertThat(textView.textSize, `is`(fontSizeBig))
        }
    }
}