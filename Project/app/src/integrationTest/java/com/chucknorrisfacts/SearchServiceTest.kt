package com.chucknorrisfacts

import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import com.chucknorrisfacts.configuration.clientApiModuleMock
import com.chucknorrisfacts.configuration.databaseModuleMock
import com.chucknorrisfacts.configuration.diferent
import com.chucknorrisfacts.model.repository.local.CategoryDao
import com.chucknorrisfacts.model.repository.local.SearchedDao
import com.chucknorrisfacts.model.service.SearchService
import com.domain.Category
import com.domain.Search
import com.domain.Searched
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.inject
import org.koin.test.KoinTest


@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchServiceTest : KoinTest {
    private val searchService: SearchService by inject()
    private val categoryDao: CategoryDao by inject()
    private val searchedDao: SearchedDao by inject()

    @Before
    fun prepare() {
        loadKoinModules(listOf(databaseModuleMock, clientApiModuleMock))
    }

    @Test
    fun Deve_retornar_lista_de_categorias__Quando_metodo_categoriesFromRemoteApiAsync_for_chamado() {
        val expectedNumberOfCategories = 16

        runBlocking {
            searchService.categoriesFromRemoteApiAsync().await().let { categories ->
                if(categories is List<*>) {
                    Assert.assertEquals(expectedNumberOfCategories, categories.size)
                } else {
                    Assert.fail("Servidor falhou a tentar buscar as categorias")
                }
            }
        }
    }

    @Test
    fun Deve_retornar_lista_de_fatos__Quando_metodo_searchWithQueryFromRemoteApiAsync_for_chamado() {
        val expectedNumberOfFacts: Long = 4
        val query = "car"

        runBlocking {
            searchService.searchWithQueryFromRemoteApiAsync(query).await().let { search ->
                if(search is Search) {
                    Assert.assertEquals(expectedNumberOfFacts, search.total)
                } else {
                    Assert.fail("Servidor falhou a tentar buscar os fatos")
                }
            }
        }
    }

    @Test
    fun Deve_retornar_lista_de_categorias__Quando_metodo_categoriesFromDatabaseAsync_for_chamado() {
        // arrange
        val expectedNumberOfCategories = 2
        val expectedName = "History"
        categoryDao.add(Category("History"))
        categoryDao.add(Category("Movie"))

        runBlocking {
            // action
            searchService.categoriesFromDatabaseAsync().await().let { categories ->
                if (categories is List<*> && categories.isNotEmpty()) {
                    // assert
                    Assert.assertEquals(expectedNumberOfCategories, categories.size)
                    Assert.assertTrue(categories[0] is Category)
                    Assert.assertEquals(expectedName, (categories[0] as Category).name)
                } else {
                    // assert
                    Assert.fail("Busca  falhou a tentar buscar as categorias")
                }
            }
        }
    }

    @Test
    fun Deve_retornar_lista_de_termos_buscados__Quando_metodo_searchedsFromDatabaseAsync_for_chamado() {
        // arrange
        val expectedNumberOfSearcheds = 3
        val expectedQuery = "Car"
        searchedDao.add(Searched("Car"))
        searchedDao.add(Searched("Fight"))
        searchedDao.add(Searched("score"))

        runBlocking {
            // action
            searchService.searchedsFromDatabaseAsync().await().let { searcheds ->
                if (searcheds is List<*> && searcheds.isNotEmpty()) {
                    // assert
                    Assert.assertEquals(expectedNumberOfSearcheds, searcheds.size)
                    Assert.assertTrue(searcheds[0] is Searched)
                    Assert.assertEquals(expectedQuery, (searcheds[0] as Searched).query)
                } else {
                    // assert
                    Assert.fail("Busca  falhou a tentar buscar as termos buscados")
                }
            }
        }
    }

    @Test
    fun Deve_salvar_lista_de_categorias__Quando_metodo_saveOnDatabaseAsync_for_chamado_com_uma_lisa_de_string() {
        // arrange
       val expectedCategories = mockCategoriesObjectFromApi()

        runBlocking {
            // action
            searchService.saveOnDatabaseAsync(expectedCategories).await()

            val categories = categoryDao.all()

            //assert
            Assert.assertEquals(expectedCategories.size, categories.size)
            categories.forEachIndexed { index, category ->
                if (expectedCategories[index].diferent(category.name!!)) {
                    Assert.fail(expectedCategories[index] + " nao foi salvo no banco de dados")
                }
            }
        }
    }

    @Test
    fun Deve_salvar_termo_buscado__Quando_metodo_saveOnDatabaseAsync_for_chamado_com_uma_string() {
        // arrange
        val expectedQuery = "fight"
        val expectedNumberOfQuerys = 1

        runBlocking {
            // action
            searchService.saveOnDatabaseAsync(expectedQuery).await()

            val searcheds = searchedDao.all()

            //assert
            Assert.assertEquals(searcheds.size, expectedNumberOfQuerys)
            Assert.assertEquals(searcheds[0].query, expectedQuery)
        }
    }
}