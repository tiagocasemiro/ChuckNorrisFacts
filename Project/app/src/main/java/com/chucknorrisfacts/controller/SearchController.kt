package com.chucknorrisfacts.controller

import com.chucknorrisfacts.model.exception.NoSuccessException
import com.chucknorrisfacts.model.repository.local.CategoryDao
import com.chucknorrisfacts.model.repository.local.SearchedDao
import com.chucknorrisfacts.model.repository.remote.ClientApi
import com.domain.Category
import com.domain.Search
import com.domain.Searched
import kotlinx.coroutines.*

class SearchController(
    private val clientApi: ClientApi, private val searchedDao: SearchedDao, private val categoryDao: CategoryDao) {

    fun categories(success: (categories: List<Category>) -> Unit, fail: () -> Unit) = GlobalScope.launch {
        if(GlobalScope.async {
            withContext(Dispatchers.Main) {
                categoriesFromDatabaseAsync().await().let {
                    if (it is List<*>) {
                        var categories = (it).map { category -> category as Category }.toList()
                        categories.let { list ->
                            if (list.isNotEmpty()) {
                                categories = list.shuffled()
                                if (categories.size > 8)
                                    categories = categories.slice(0..7)
                                success(categories)
                                return@withContext false
                            } else {
                                return@withContext true
                            }
                        }
                    } else {
                        return@withContext false
                    }
                }
            }
        }.await()) {
            withContext(Dispatchers.Main) {
                when (val objectReturned = categoriesFromRemoteApiAsync().await()) {
                    is List<*> -> {
                        this@SearchController.saveOnDatabaseAsync(objectReturned).start()
                        var categories = objectReturned.map { Category(it as String) }.toList()
                        categories = categories.shuffled()
                        if (categories.size > 8)
                            categories = categories.slice(0..7)
                        success(categories)
                    }
                    else -> {
                        fail()
                    }
                }
            }
        }
    }

    fun searchWith(query: String, success: (search: Search) -> Unit, fail: () -> Unit) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            when (val objectReturned = searchWithQueryFromRemoteApiAsync(query).await()) {
                is Search -> {
                    success(objectReturned)
                } else -> {
                    fail()
                }
            }
        }
        this@SearchController.saveOnDatabaseAsync(query).start()
    }

    fun searchWith(query: String, success: (search: Search) -> Unit, fail: () -> Unit,
                   successSearcheds : (searcheds: List<Searched>) -> Unit) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            when (val objectReturned = searchedsFromDatabaseAsync().await()) {
                is List<*> -> {
                    val searcheds = objectReturned.map { it as Searched }.toMutableList()
                    if(searcheds.contains(Searched(query))) {
                        searcheds.remove(Searched(query))
                    }
                    searcheds.add(Searched(query))
                    successSearcheds(searcheds)
                } else -> {
                    val searcheds = mutableListOf<Searched>()
                    searcheds.add(Searched(query))
                    successSearcheds(searcheds)
                }
            }
        }
        this@SearchController.searchWith(query, success, fail)
    }

    fun searcheds(success: (facts: List<Searched>) -> Unit, fail: () -> Unit) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            when (val objectReturned = searchedsFromDatabaseAsync().await()) {
                is List<*> -> {
                    success(objectReturned.map { it as Searched }.toList())
                } else -> {
                    fail()
                }
            }
        }
    }

    private fun categoriesFromRemoteApiAsync() = GlobalScope.async(Dispatchers.IO) {
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

    private fun searchWithQueryFromRemoteApiAsync(query: String) = GlobalScope.async(Dispatchers.IO) {
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

    private fun categoriesFromDatabaseAsync() = GlobalScope.async(Dispatchers.IO) {
        return@async try {
            categoryDao.all()
        } catch (exception: Exception) {
            exception.printStackTrace()
            exception
        }
    }

    private fun searchedsFromDatabaseAsync() = GlobalScope.async(Dispatchers.IO)  {
        try {
            return@async searchedDao.all()
        } catch (e: Exception) {
            e.printStackTrace()
            return@async e
        }
    }

    private fun saveOnDatabaseAsync(categories: List<*>) = GlobalScope.async(Dispatchers.IO) {
        try {
            categories.forEach {
                categoryDao.add(Category(it as String))
            }
        } catch (e: Exception) {
           e.printStackTrace()
        }
    }

    private fun saveOnDatabaseAsync(query: String) = GlobalScope.async(Dispatchers.IO) {
        try {
            searchedDao.add(Searched(query))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}