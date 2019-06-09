package com.chucknorrisfacts.view


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.chucknorrisfacts.R
import com.chucknorrisfacts.configuration.NavigationTestRule
import com.domain.Search
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class InicialState {

    @Rule
    @JvmField
    var mActivityTestRule = NavigationTestRule( MainActivity::class.java,
        initialTouchMode = true,
        launchActivity = false
    )


    @Before
    fun prepare() {

    }

    @Test
    fun inicialState() {
        val search = Search()
        search.total = 0

        val initialIntent= Intent()
        val arguments = Bundle().apply {
            putSerializable(Search::class.java.canonicalName, search)
        }
        initialIntent.putExtras(arguments)
        mActivityTestRule.navigationId = R.id.action_factsFragment_to_searchFragment
        mActivityTestRule.launchActivity(initialIntent)

        Thread.sleep(10000)

        val imageButton = onView( allOf( withId(R.id.search), isDisplayed() ) )

        imageButton.check(matches(isDisplayed()))

        val frameLayout = onView(allOf(withId(R.id.fragmentHost), isDisplayed()))
        frameLayout.check(matches(isDisplayed()))

        val view = onView( allOf( withId(android.R.id.statusBarBackground),isDisplayed() ) )

        view.check(matches(isDisplayed()))

        val frameLayout2 = onView(  allOf(   isDisplayed()  ))


        frameLayout2.check(matches(isDisplayed()))



    }
}
