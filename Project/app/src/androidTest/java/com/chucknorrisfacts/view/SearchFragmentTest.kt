package com.chucknorrisfacts.view


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.chucknorrisfacts.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun searchTest() {
        val floatingActionButton = onView(
            allOf(
                withId(R.id.search),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fragmentHost),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.query),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.queryInputLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("car"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.query), withText("car"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.queryInputLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(pressImeActionButton())

        val recyclerView = onView(
            allOf(
                withId(R.id.facts),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fragmentHost),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val materialCardView = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.facts),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialCardView.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
