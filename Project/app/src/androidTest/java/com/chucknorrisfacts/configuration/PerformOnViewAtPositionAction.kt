package com.chucknorrisfacts.configuration

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.chucknorrisfacts.view.FactsAdapter
import org.hamcrest.Matcher
import org.hamcrest.Matchers


class PerformOnViewAtPositionAction(private val position: Int, @IdRes private val viewId: Int, private val action: ViewAction) : ViewAction {

    override fun getDescription(): String {
        return "Execute action on view at position"
    }

    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf<View>(ViewMatchers.isAssignableFrom(RecyclerView::class.java),ViewMatchers.isDisplayed())
    }

    override fun perform(uiController: UiController?, view: View?) {
        val recyclerView = view as RecyclerView
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as FactsAdapter.FactViewHolder
        val viewAction = viewHolder.itemView.findViewById<View>(viewId)
        action.perform(uiController, viewAction)
        uiController?.loopMainThreadUntilIdle()
    }
}