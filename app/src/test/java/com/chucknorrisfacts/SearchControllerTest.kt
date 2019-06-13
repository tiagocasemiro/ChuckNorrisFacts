package com.chucknorrisfacts

import com.chucknorrisfacts.controller.SearchController
import com.chucknorrisfacts.model.repository.remote.FactDeserialize
import com.chucknorrisfacts.model.service.SearchService
import com.domain.Fact
import com.domain.Searched
import com.google.gson.Gson
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

    @MockK
    lateinit var searchService: SearchService

    private val gson: Gson = GsonBuilder().registerTypeAdapter(Fact::class.java,  FactDeserialize()).create()

    @Before
    fun prepare() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `Deve retornar uma lista com 8 categorias aleatorias do banco - Quando metodo categories for chamado`() {
        // arrange
        every { searchService.categoriesFromDatabaseAsync() } returns GlobalScope.async { mockCategoriesFromDb() }
        every { searchService.categoriesFromRemoteApiAsync() } answers { GlobalScope.async {
            Assert.fail("Busca no banco local falhou")
        } }
        val expectNumberOfCategories = 8
        val noExpectedArray = mockCategoriesFromDb().slice(0..7)
        val searchController = SearchController(searchService)

        runBlocking {
            // action
            searchController.categories({ categories ->
                // assert
                Assert.assertEquals(expectNumberOfCategories, categories.size)
                Assert.assertFalse(Arrays.equals(noExpectedArray.toTypedArray(), categories.toTypedArray()))
            }, {
                // assert
                Assert.fail("Busca no banco local falhou")
            }).join()
        }
    }

    @Test
    fun `Deve retornar uma lista com 8 categorias aleatorias da api - Quando metodo categories for chamado e busca no banco falhar`() {
        // arrange
        every { searchService.categoriesFromDatabaseAsync() } returns GlobalScope.async { Exception() }
        every { searchService.categoriesFromRemoteApiAsync() } answers { GlobalScope.async { mockCategoriesFromApi() }}
        every { searchService.saveOnDatabaseAsync(any<List<*>>()) } answers { GlobalScope.async {  } }

        val expectNumberOfCategories = 8
        val noExpectedArray = mockCategoriesFromDb().slice(0..7)
        val searchController = SearchController(searchService)

        runBlocking {
            // action
            searchController.categories({ categories ->
                // assert
                Assert.assertEquals(expectNumberOfCategories, categories.size)
                Assert.assertFalse(Arrays.equals(noExpectedArray.toTypedArray(), categories.toTypedArray()))
            }, {
                // assert
                Assert.fail("Busca no banco local falhou")
            }).join()
        }
    }

    @Test
    fun `Deve retornar falha - Quando metodo categories falhar nas buscas no banco e na api`() {
        // arrange
        every { searchService.categoriesFromDatabaseAsync() } returns GlobalScope.async { Exception() }
        every { searchService.categoriesFromRemoteApiAsync() } answers { GlobalScope.async { Exception() } }

        val searchController = SearchController(searchService)

        runBlocking {
            // action
            searchController.categories({
                // assert
                Assert.fail("Busca deve apresentar falha")
            }, {
                // assert
                Assert.assertTrue(true)
            }).join()
        }
    }

    @Test
    fun `Deve retornar uma lista de Fatos - Quando metodo searchWith for chamdo com uma query`() {
        // arrange
        every { searchService.searchWithQueryFromRemoteApiAsync(any()) } returns GlobalScope.async {  mockSearchFromApi() }
        every { searchService.saveOnDatabaseAsync(any<String>()) } answers { GlobalScope.async { } }
        val searchController = SearchController(searchService)
        val expectedNumberOfFacts = 4
        val anyQueryToMock = "any query"

        runBlocking {
            // action
            searchController.searchWith(anyQueryToMock, { facts ->
                // assert
                Assert.assertEquals(expectedNumberOfFacts, facts.total!!.toInt())
            }, {
                // assert
                Assert.fail("Busca de fatos falhou")
            }).join()
        }
    }

    @Test
    fun `Deve retornar uma lista de Fatos e uma lista de termos buscados nao duplicados e em ordem reversa - Quando metodo searchWith for chamdo com uma query que ja foi buscada`() {
        // arrange
        every { searchService.searchWithQueryFromRemoteApiAsync(any()) } returns GlobalScope.async {  mockSearchFromApi() }
        every { searchService.searchedsFromDatabaseAsync() } returns GlobalScope.async { mockSearchedFromDb() }
        every { searchService.saveOnDatabaseAsync(any<String>()) } answers { GlobalScope.async { } }
        val searchController = SearchController(searchService)
        val expectedNumberOfFacts = 4
        val expectedNumberOfSearcheds = 6
        val expectedDuplicationElements = 0
        val query = "car"

        runBlocking {
            // action
            searchController.searchWith(query, { facts ->
                // assert
                Assert.assertEquals(expectedNumberOfFacts, facts.total!!.toInt())
            }, {
                // assert
                Assert.fail("Busca de fatos falhou")
            }, { searcheds ->
                // assert
                Assert.assertEquals(expectedNumberOfSearcheds, searcheds.size)
                Assert.assertEquals(expectedDuplicationElements, searcheds.groupingBy { it }.eachCount().filter { it.value > 1 }.size)
            }).join()
        }
    }

    @Test
    fun `Deve retornar uma lista de Fatos e uma lista de termos buscados nao duplicados e em ordem reversa - Quando metodo searchWith for chamdo com uma query que nao foi buscada`() {
        // arrange
        every { searchService.searchWithQueryFromRemoteApiAsync(any()) } returns GlobalScope.async {  mockSearchFromApi() }
        every { searchService.searchedsFromDatabaseAsync() } returns GlobalScope.async { mockSearchedFromDb() }
        every { searchService.saveOnDatabaseAsync(any<String>()) } answers { GlobalScope.async { } }
        val searchController = SearchController(searchService)
        val query = "pop star"
        val expectedNumberOfFacts = 4
        val expectedNumberOfSearcheds = 7
        val expectedDuplicationElements = 0
        val mockSearchedFromDb = mockSearchedFromDb().toMutableList()
        mockSearchedFromDb.add(Searched(query))
        val expectedReversedArray = mockSearchedFromDb.reversed()

        runBlocking {
            // action
            searchController.searchWith(query, { facts ->
                // assert
                Assert.assertEquals(expectedNumberOfFacts, facts.total!!.toInt())
            }, {
                // assert
                Assert.fail("Busca de fatos falhou")
            }, { searcheds ->
                // assert
                Assert.assertEquals(expectedNumberOfSearcheds, searcheds.size)
                Assert.assertEquals(expectedDuplicationElements, searcheds.groupingBy { it }.eachCount().filter { it.value > 1 }.size)
                Assert.assertArrayEquals(expectedReversedArray.toTypedArray(), searcheds.toTypedArray())
            }).join()
        }
    }

    @Test
    fun `Deve retornar uma falha - Quando metodo searchWith for chamdo com uma query`() {
        // arrange
        every { searchService.searchWithQueryFromRemoteApiAsync(any()) } returns GlobalScope.async { Exception() }
        every { searchService.saveOnDatabaseAsync(any<String>()) } answers { GlobalScope.async { } }
        val searchController = SearchController(searchService)
        val anyQueryToMock = "any query"

        runBlocking {
            // action
            searchController.searchWith(anyQueryToMock, {
                // assert
                Assert.fail("Busca de fatos deve falhar")
            }, {
                // assert
                Assert.assertTrue(true)
            }).join()
        }
    }

    @Test
    fun `Deve retornar uma falha - Quando metodo searchWith for chamdo com uma query e um callback de termos buscados`() {
        // arrange
        every { searchService.searchWithQueryFromRemoteApiAsync(any()) } returns GlobalScope.async { Exception() }
        every { searchService.saveOnDatabaseAsync(any<String>()) } answers { GlobalScope.async { } }
        every { searchService.searchedsFromDatabaseAsync() } returns GlobalScope.async { Exception() }
        val searchController = SearchController(searchService)
        val anyQueryToMock = "any query"
        val expectedSizeShearteds = 1

        runBlocking {
            // action
            searchController.searchWith(anyQueryToMock, {
                // assert
                Assert.fail("Busca de fatos deve falhar")
            }, {
                // assert
                Assert.assertTrue(true)
            }, { searcheds ->
                Assert.assertEquals(expectedSizeShearteds, searcheds.size)
            }).join()
        }
    }

    @Test
    fun `Deve retornar uma lista de termos buscados nao repetidos - Quando metodo searcheds for chamado`() {
        // arrange
        every { searchService.searchedsFromDatabaseAsync() } returns GlobalScope.async { mockSearchedFromDb() }
        val searchController = SearchController(searchService)
        val expectedNumberOfSearcheds = 6
        val expectedDuplicationElements = 0

        runBlocking {
            // action
            searchController.searcheds(
                { searcheds ->
                    // assert
                    Assert.assertEquals(expectedNumberOfSearcheds, searcheds.size)
                    Assert.assertEquals(expectedDuplicationElements, searcheds.groupingBy { it }.eachCount().filter { it.value > 1 }.size)
                }, {
                    // assert
                    Assert.fail("Busca de searcheds falhou")
                }
            ).join()
        }
    }

    @Test
    fun `Deve retornar uma lista de termos buscados em ordem reversa - Quando metodo searcheds for chamado`() {
        // arrange
        every { searchService.searchedsFromDatabaseAsync() } returns GlobalScope.async { mockSearchedFromDb() }
        val searchController = SearchController(searchService)
        val expectedNumberOfSearcheds = 6
        val expectedReversedArray = mockSearchedFromDb().reversed()

        runBlocking {
            // action
            searchController.searcheds(
                { searcheds ->
                    // assert
                    Assert.assertEquals(expectedNumberOfSearcheds, searcheds.size)
                    Assert.assertArrayEquals(expectedReversedArray.toTypedArray(), searcheds.toTypedArray())
                }, {
                    // assert
                    Assert.fail("Busca de searcheds falhou")
                }
            ).join()
        }
    }

    @Test
    fun `Deve falhar - Quando metodo searcheds for chamado`() {
        // arrange
        every { searchService.searchedsFromDatabaseAsync() } returns GlobalScope.async { Exception() }
        val searchController = SearchController(searchService)

        runBlocking {
            // action
            searchController.searcheds(
                {
                    // assert
                    Assert.fail("Busca de Serarcheds deve falahar")
                }, {
                    // assert
                    Assert.assertTrue(true)
                }
            ).join()
        }
    }

    @Test
    fun `Deve retornar o label uncategorized - Quando categoria nao ter atributo categories no json de fatos`() {
        // arrange
        val json = "{\n" +
                "\"icon_url\": \"https://assets.chucknorris.host/img/avatar/chuck-norris.png\",\n" +
                "\"id\": \"6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"url\": \"https://api.chucknorris.io/jokes/6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"value\": \"Chuck Norris killed The Fat Lady because it's not over until Chuck Norris sings.\"\n" +
                "}"

        // action
        val fact = gson.fromJson<Fact>(json, Fact::class.java)

        //assert
        Assert.assertEquals(fact.category?.name, "uncategorized")
    }

    @Test
    fun `Deve retornar o label uncategorized - Quando categoria for nula no json de fatos`() {
        // arrange
        val json = "{\n" +
                "\"category\": null,\n" +
                "\"icon_url\": \"https://assets.chucknorris.host/img/avatar/chuck-norris.png\",\n" +
                "\"id\": \"6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"url\": \"https://api.chucknorris.io/jokes/6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"value\": \"Chuck Norris killed The Fat Lady because it's not over until Chuck Norris sings.\"\n" +
                "}"

        // action
        val fact = gson.fromJson<Fact>(json, Fact::class.java)

        // assert
        Assert.assertEquals(fact.category?.name, "uncategorized")
    }
}