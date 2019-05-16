package com.chucknorrisfacts.controller

import com.chucknorrisfacts.model.repository.exception.ExpectedException
import com.chucknorrisfacts.model.repository.exception.NoResultException
import com.chucknorrisfacts.model.repository.local.CategoryDao
import com.chucknorrisfacts.model.repository.local.SearchedDao
import com.chucknorrisfacts.model.repository.remote.ClientApi
import com.domain.Category
import com.domain.Fact
import com.domain.Searched
import com.google.gson.Gson
import kotlinx.coroutines.*

class SearchController(private val clientApi: ClientApi, private val searchedDao: SearchedDao, private val categoryDao: CategoryDao) {

    fun categories(success: (categories: List<Category>) -> Unit, fail: (exception: Exception) -> Unit) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            val objectReturned = categoriesFromRemoteApi().await()
            when (objectReturned) {
                is List<*> -> {
                    this@SearchController.saveOnDatabase(objectReturned)
                    success(objectReturned.map { Category(it as String) }.toList())
                }
                else -> {
                    categoriesFromDatabase().let {
                        when (objectReturned) {
                            is List<*> -> { success((it as List<*>).map { it as Category }.toList()) }
                            is NoResultException -> { fail(objectReturned) }
                            is Exception -> { fail(objectReturned) }
                            else -> { fail(ExpectedException()) }
                        }
                    }
                }
            }
        }
    }

    fun searchWith(query: String, success: (facts: List<Fact>) -> Unit, fail: (exception: Exception) -> Unit) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            val objectReturned = searchWithQueryFromRemoteApi(query).await()
            when (objectReturned) {
                is List<*> -> {
                    success(objectReturned.map { it as Fact }.toList())
                }
                else -> {
                    when (objectReturned) {
                        is NoResultException -> { fail(objectReturned) }
                        is Exception -> { fail(objectReturned) }
                        else -> { fail(ExpectedException()) }
                    }
                }
            }
        }
    }

    fun searcheds(success: (facts: List<Searched>) -> Unit, fail: (exception: Exception) -> Unit) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            val objectReturned = searchedsFromDatabase()
            when (objectReturned) {
                is List<*> -> {
                    success(objectReturned.map { it as Searched }.toList())
                }
                else -> {
                    fail(objectReturned as Exception)
                }
            }
        }
    }

    private fun categoriesFromRemoteApi() = GlobalScope.async(Dispatchers.IO) {
        try {
            val response = clientApi.categories().execute()
            if (response.isSuccessful) {
                return@async response.body()!!
            } else {
                return@async NoResultException()
            }
        } catch (exception: Exception) {
            return@async exception
        } catch (throwable: Throwable) {
            return@async Exception(throwable)
        }
    }

    private fun searchWithQueryFromRemoteApi(query: String) = GlobalScope.async(Dispatchers.IO) {
        try {
            val response = clientApi.search(query).execute()
            this@SearchController.saveOnDatabase(query)
            if (response.isSuccessful)
                return@async response.body()!!
            else
                return@async Gson().toJson(emptyList<Category>())
        } catch (exception: Exception) {
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