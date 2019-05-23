package com.chucknorrisfacts.controller

import com.chucknorrisfacts.model.exception.NoSuccessException
import com.chucknorrisfacts.model.repository.local.CategoryDao
import com.chucknorrisfacts.model.repository.local.SearchedDao
import com.chucknorrisfacts.model.repository.remote.ClientApi
import com.domain.Category
import com.domain.Search
import com.domain.Searched
import kotlinx.coroutines.*

class SearchController(private val clientApi: ClientApi, private val searchedDao: SearchedDao, private val categoryDao: CategoryDao) {

    fun categories(success: (categories: List<Category>) -> Unit, fail: () -> Unit) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            val objectReturned = categoriesFromRemoteApi().await()
            when (objectReturned) {
                is List<*> -> {
                    this@SearchController.saveOnDatabase(objectReturned)
                    success(objectReturned.map { Category(it as String) }.toList())
                }
                else -> {
                    categoriesFromDatabase().await().let {
                        if(it is List<*>) {
                            success((it).map { it as Category }.toList())
                        } else {
                            fail()
                        }
                    }
                }
            }
        }
    }

    fun searchWith(query: String, success: (search: Search) -> Unit, fail: () -> Unit) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            val objectReturned = searchWithQueryFromRemoteApi(query).await()
            this@SearchController.saveOnDatabase(query)
            when (objectReturned) {
                is Search -> {
                    success(objectReturned)
                } else -> {
                    fail()
                }
            }
        }
    }

    fun searcheds(success: (facts: List<Searched>) -> Unit, fail: () -> Unit) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            val objectReturned = searchedsFromDatabase().await()
            when (objectReturned) {
                is List<*> -> {
                    success(objectReturned.map { it as Searched }.toList())
                } else -> {
                    fail()
                }
            }
        }
    }

    private fun categoriesFromRemoteApi() = GlobalScope.async(Dispatchers.IO) {
        try {
            val response = clientApi.categories().execute()
            if (response.isSuccessful)
                return@async response.body()!!
             else
                throw NoSuccessException()
        } catch (exception: NoSuccessException) {
            return@async exception
        } catch (throwable: Throwable) {
            return@async Exception(throwable)
        }
    }

    private fun searchWithQueryFromRemoteApi(query: String) = GlobalScope.async(Dispatchers.IO) {
        try {
            val response = clientApi.search(query).execute()
            if (response.isSuccessful)
                return@async response.body()!!
            else
                throw NoSuccessException()
        } catch (exception: NoSuccessException) {
            return@async exception
        } catch (throwable: Throwable) {
            return@async Exception(throwable)
        }
    }

    private fun categoriesFromDatabase() = GlobalScope.async(Dispatchers.IO) {
        return@async try {
            categoryDao.all()
        } catch (exception: Exception) {
            exception.printStackTrace()
            exception
        }
    }

    private fun searchedsFromDatabase() = GlobalScope.async(Dispatchers.IO)  {
        try {
            return@async searchedDao.all()
        } catch (e: Exception) {
            e.printStackTrace()
            return@async e
        }
    }

    private fun saveOnDatabase(categories: List<*>) = GlobalScope.async(Dispatchers.IO) {
        try {
            categories.forEach {
                categoryDao.add(Category(it as String))
            }
        } catch (e: Exception) {
           e.printStackTrace()
        }
    }

    private fun saveOnDatabase(query: String) = GlobalScope.async(Dispatchers.IO) {
        try {
            searchedDao.add(Searched(query))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}