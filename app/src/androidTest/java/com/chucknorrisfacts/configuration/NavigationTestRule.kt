package com.chucknorrisfacts.configuration

import android.app.Activity
import androidx.annotation.IdRes
import androidx.navigation.Navigation
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.chucknorrisfacts.R


class NavigationTestRule<A : Activity>(activityClass: Class<A>?, initialTouchMode: Boolean, launchActivity: Boolean) : IntentsTestRule<A>(activityClass, initialTouchMode, launchActivity) {

    @IdRes
    var navigationId: Int? = null

    override fun afterActivityLaunched() {
        super.afterActivityLaunched()
        navigationId?.let {
            activity.runOnUiThread {
                Navigation.findNavController(activity!!, R.id.fragmentHost).navigate(navigationId!!)
            }
        }
    }
}