package com.chucknorrisfacts

import com.chucknorrisfacts.controller.SearchController
import com.chucknorrisfacts.model.repository.remote.FactDeserialize
import com.chucknorrisfacts.model.service.SearchService
import com.domain.Category
import com.domain.Fact
import com.google.gson.GsonBuilder
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class SearchControllerTest {

    //Unit Test

    @MockK
    lateinit var searchService: SearchService

    private val categories: MutableList<Category> = mutableListOf()


    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        categories.add(Category("fashion"))
        categories.add(Category("money"))
        categories.add(Category("career"))
        categories.add(Category("travel"))
        categories.add(Category("music"))
        categories.add(Category("history"))
        categories.add(Category("animal"))
        categories.add(Category("religion"))
        categories.add(Category("political"))
        categories.add(Category("sport"))
        categories.add(Category("science"))
        categories.add(Category("celebrity"))
        categories.add(Category("food"))
        categories.add(Category("movie"))
        categories.add(Category("dev"))
        categories.add(Category("explicit"))
    }

    @Test
    fun `Deve retornar uma lista com 8 categorias aleatorias - Quando metodo categories encontrar registros no banco local`() {
        // arrange
        every { searchService.categoriesFromDatabaseAsync() } returns GlobalScope.async { categories.toList() }
        every { searchService.categoriesFromRemoteApiAsync() } answers { GlobalScope.async { } }
        val expectNumberOfCategories = 8
        val noExpectedArray = categories.toList().slice(0..7)
        val searchController = SearchController(searchService)

        runBlocking {
            // action
            searchController.categories({ categories ->
                // assert
                Assert.assertEquals(expectNumberOfCategories, categories.size)
                Assert.assertFalse(Arrays.equals(noExpectedArray.toTypedArray(), categories.toTypedArray()))
            }, {
                // assert
                Assert.fail()
            }).join()
        }
    }

    @Test
    fun `Deve retornar uma lista com 8 categorias aleatorias - Quando metodo categories nao encontrar registro local deve buscar na api`() {

    }

    @Test
    fun `Deve retornar uma lista de Fatos - Quando metodo searchWith for chamdo com uma query`() {

    }

    @Test
    fun `Deve retornar uma lista de Fatos e uma lista de termos buscados - Quando metodo searchWith for chamdo com uma query`() {

    }

    @Test
    fun `Deve retornar uma lista de termos nao repetidos buscados - Quando metodo searcheds for chamado`() {

    }

    @Test
    fun `Deve falhar - Quando metodo searcheds for chamado`() {

    }

    @Test
    fun `Deve retornar uma lista de termos ordenada como uma pilha - Quando metodo searcheds for chamado`() {

    }

    @Test
    fun `Deve retornar o label uncategorized - Quando category nao ter atributo categories no json fact`() {
        val json = "{\n" +
                "\"icon_url\": \"https://assets.chucknorris.host/img/avatar/chuck-norris.png\",\n" +
                "\"id\": \"6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"url\": \"https://api.chucknorris.io/jokes/6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"value\": \"Chuck Norris killed The Fat Lady because it's not over until Chuck Norris sings.\"\n" +
                "}"

        val gsonBuilder = GsonBuilder()
        val deserializer = FactDeserialize()
        gsonBuilder.registerTypeAdapter(Fact::class.java, deserializer)
        val gson = gsonBuilder.create()
        val fact = gson.fromJson<Fact>(json, Fact::class.java)

        Assert.assertEquals(fact.category?.name, "uncategorized")
    }

    @Test
    fun `Deve retornar o label uncategorized - Quando categories for null no json fact`() {
        val json = "{\n" +
                "\"category\": null,\n" +
                "\"icon_url\": \"https://assets.chucknorris.host/img/avatar/chuck-norris.png\",\n" +
                "\"id\": \"6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"url\": \"https://api.chucknorris.io/jokes/6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"value\": \"Chuck Norris killed The Fat Lady because it's not over until Chuck Norris sings.\"\n" +
                "}"

        val gsonBuilder = GsonBuilder()
        val deserializer = FactDeserialize()
        gsonBuilder.registerTypeAdapter(Fact::class.java, deserializer)
        val gson = gsonBuilder.create()
        val fact = gson.fromJson<Fact>(json, Fact::class.java)

        Assert.assertEquals(fact.category?.name, "uncategorized")
    }
}