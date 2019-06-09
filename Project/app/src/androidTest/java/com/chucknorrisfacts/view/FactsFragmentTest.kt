package com.chucknorrisfacts.view

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.chucknorrisfacts.R
import com.chucknorrisfacts.configuration.*
import com.chucknorrisfacts.mockSearchFromIntent
import com.domain.Search
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest


@LargeTest
@RunWith(AndroidJUnit4::class)
class FactsFragmentTest : KoinTest {

    @Rule
    @JvmField
    var activityTestRule = NavigationTestRule(MainActivity::class.java,
        initialTouchMode = true,
        launchActivity = false
    )

    @Before
    fun prepare() {
        StandAloneContext.loadKoinModules(listOf(databaseModuleMock, clientApiModuleMock))
    }

    @Test
    fun deve_exibir_tela_em_branco_com_um_unico_botao_de_busca__Quando_a_aplicacao_e_iniciada() {
        // action
        activityTestRule.launchActivity(null)

        // assert
        onView(allOf(withId(R.id.noResult))).check(matches(not(isDisplayed())))
        onView(allOf(withId(R.id.facts))).check(matches(not(isDisplayed())))
        onView(allOf(withId(R.id.search))).check(matches(isDisplayed()))
    }

    @Test
    fun deve_exibir_uma_mensagem__Quando_uma_busca_nao_obtem_resultados() {
        // arrange
        val search = Search()
        search.total = 0
        val intent = Intent()
        val arguments = Bundle().apply {
            putSerializable(Search::class.java.canonicalName, search)
        }
        intent.putExtras(arguments)

        // action
        activityTestRule.launchActivity(intent)

        // assert
        onView(allOf(withId(R.id.noResult))).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.facts))).check(matches(not(isDisplayed())))
        onView(allOf(withId(R.id.search))).check(matches(isDisplayed()))
    }

    @Test
    fun deve_exibir_uma_lista_de_fatos__Quando_uma_busca_obtem_resultados() {
        // arrange
        val intent = Intent()
        val arguments = Bundle().apply {
            putSerializable(Search::class.java.canonicalName, mockSearchFromIntent())
        }
        intent.putExtras(arguments)

        // action
        activityTestRule.launchActivity(intent)

        // assert
        onView(withId(R.id.noResult)).check(matches(not(isDisplayed())))
        onView(withId(R.id.facts)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).check(matches(isDisplayed()))
    }

    @Test
    fun deve_exibir_uma_lista_com_quatro_fatos__Quando_uma_busca_obtem_resultados() {
        // arrange
        val intent = Intent()
        val arguments = Bundle().apply {
            putSerializable(Search::class.java.canonicalName, mockSearchFromIntent())
        }
        intent.putExtras(arguments)

        // action
        activityTestRule.launchActivity(intent)

        // assert
        onView(withId(R.id.facts)).check(RecyclerViewItemCountAssertion(4))
    }

    @Test
    fun deve_exibir_dados_esperados_no_primeiro_card__Quando_uma_busca_obtem_resultados() {
        // arrange
        val expectedDescription = "Chuck Norris always knows the EXACT location of Carmen SanDiego."
        val expectedCategory = "movie"
        val intent = Intent()
        val arguments = Bundle().apply {
            putSerializable(Search::class.java.canonicalName, mockSearchFromIntent())
        }
        intent.putExtras(arguments)

        // action
        activityTestRule.launchActivity(intent)

        onView(allOf(withId(R.id.description), withText(expectedDescription)))
            .check(matches(isCompletelyDisplayed()))

        onView(allOf(withId(R.id.category), withText(expectedCategory)))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun deve_aumentar_a_font_da_descricao_com_mais_de_80_caracteres__Quando_uma_busca_obtem_resultados() {
        // arrange
        val intent = Intent()
        val arguments = Bundle().apply {
            putSerializable(Search::class.java.canonicalName, mockSearchFromIntent())
        }
        intent.putExtras(arguments)

        // action
        activityTestRule.launchActivity(intent)

        onView(withId(R.id.facts)).perform(ScrollToPositionAction(3))

        onView(allOf(
            withId(R.id.description),
            withText("Chuck norris is only scared of one thing his reflection fact"),
            isDisplayed()))
        .check(TextViewFontSizeAssertion())
    }

    @Test
    fun deve_lancar_uma_intent_action_send_e_a_url_do_fato__Quando_clicar_no_botao_compartilhar_do_card() {
        // arrange
        val expectedShareUrl = "https://api.chucknorris.io/jokes/tng5xzi5t9syvqaubukycw"
        val intent = Intent()
        val arguments = Bundle().apply {
            putSerializable(Search::class.java.canonicalName, mockSearchFromIntent())
        }
        intent.putExtras(arguments)
        activityTestRule.launchActivity(intent)
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith( Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))

        // action
        onView(withId(R.id.facts)).perform(PerformOnViewAtPositionAction(0, R.id.share, click()))

        // assert
        intended(allOf(hasAction(Intent.ACTION_CHOOSER), hasExtra(`is`(Intent.EXTRA_INTENT), allOf(
            hasAction(Intent.ACTION_SEND),
            hasExtra(Intent.EXTRA_TEXT, expectedShareUrl)
        ))))
    }
}
