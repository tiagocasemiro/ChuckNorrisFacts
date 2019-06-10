package com.chucknorrisfacts.view

import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.chucknorrisfacts.R
import com.chucknorrisfacts.configuration.NavigationTestRule
import com.chucknorrisfacts.configuration.clientApiModuleMock
import com.chucknorrisfacts.configuration.databaseModuleMock
import com.chucknorrisfacts.model.repository.local.SearchedDao
import com.domain.Searched
import org.hamcrest.Description
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest


@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchFragmentTest : KoinTest {

    private val searchedDao: SearchedDao by inject()
    private val nothing = null

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

    @Test
    fun deve_efetuar_uma_busca__Quando_inserir_um_texto_no_input_e_clicar_na_tecla_done_do_teclado() {
        activityTestRule.launchActivity(nothing)

        // arrange
        onView(withId(R.id.query)).perform(click(), typeText("car"))

        // action
        onView(withId(R.id.query)).perform(pressImeActionButton())

        // assert
        onView(withId(R.id.noResult)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.facts)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).check(matches(isDisplayed()))
    }

    @Test
    fun deve_falhar__Quando_clicar_no_done_do_teclado_sem_digitar_um_texto_no_input() {
        activityTestRule.launchActivity(nothing)

        // arrange
        onView(withId(R.id.query)).perform(click())

        // action
        onView(withId(R.id.query)).perform(pressImeActionButton())

        // assert
        onView(withId(R.id.textinput_error)).check(matches(allOf(withText("Required field"), isDisplayed())))
    }

    @Test
    fun deve_ter_input_de_texto_com_tecla_submissao_done_no_teclado__Quando_abrir_tela_de_busca() {
        activityTestRule.launchActivity(nothing)

        // assert
        onView(withId(R.id.query)).check(matches(allOf(
            withInputType(InputType.TYPE_CLASS_TEXT), hasImeAction(EditorInfo.IME_ACTION_DONE)
        )))
    }

    @Test
    fun deve_fazer_uma_busca_com_a_categoria__Quando_clicar_em_uma_categoria() {
        activityTestRule.launchActivity(nothing)

        // action
        onView(object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description) {
                    description.appendText("Child at position 0 in parent ")
                    withId(R.id.chipGroup).describeTo(description)
                }

                public override fun matchesSafely(view: View): Boolean {
                    val parent = view.parent
                    return parent is ViewGroup && withId(R.id.chipGroup).matches(parent) && view == parent.getChildAt(0)
                }
            }).perform(click())

        // assert
        onView(withId(R.id.noResult)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.facts)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).check(matches(isDisplayed()))
    }

    @Test
    fun deve_fazer_uma_busca_com_o_termo__Quando_clicar_em_um_item_na_lista_de_termos_buscados() {
        // arrange
        searchedDao.add(Searched("car"))
        activityTestRule.launchActivity(nothing)

        // action
        onView(withText("car")).perform(click())

        // assert
        onView(withId(R.id.noResult)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.facts)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).check(matches(isDisplayed()))
    }
}
