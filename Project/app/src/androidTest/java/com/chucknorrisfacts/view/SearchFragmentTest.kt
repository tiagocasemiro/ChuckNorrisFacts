package com.chucknorrisfacts.view

import androidx.test.filters.LargeTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chucknorrisfacts.R
import com.chucknorrisfacts.configuration.NavigationTestRule
import com.chucknorrisfacts.configuration.clientApiModuleMock
import com.chucknorrisfacts.configuration.databaseModuleMock
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest


@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchFragmentTest : KoinTest {

    @Rule
    @JvmField
    var activityTestRule = NavigationTestRule(MainActivity::class.java,
        initialTouchMode = true,
        launchActivity = false
    )

    @Before
    fun prepare() {
        activityTestRule.navigationId = R.id.action_factsFragment_to_searchFragment
        StandAloneContext.loadKoinModules(listOf(databaseModuleMock, clientApiModuleMock))
    }
}
