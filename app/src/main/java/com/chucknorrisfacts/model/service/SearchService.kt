package com.chucknorrisfacts.model.service

import com.chucknorrisfacts.model.NoSuccessException
import com.chucknorrisfacts.model.repository.local.CategoryDao
import com.chucknorrisfacts.model.repository.local.SearchedDao
import com.chucknorrisfacts.model.repository.remote.ClientApi
import com.domain.Category
import com.domain.Searched
import kotlinx.coroutines.*


class SearchService(private val clientApi: ClientApi, private val searchedDao: SearchedDao, private val categoryDao: CategoryDao) {

    fun categoriesFromRemoteApiAsync() = CoroutineScope(Job()).async(Dispatchers.IO) {
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

    fun searchWithQueryFromRemoteApiAsync(query: String) = CoroutineScope(Job()).async(Dispatchers.IO) {
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

    fun categoriesFromDatabaseAsync() = CoroutineScope(Job()).async(Dispatchers.IO) {
        return@async try {
            categoryDao.all()
        } catch (exception: Exception) {
            exception.printStackTrace()
            exception
        }
    }

    fun searchedsFromDatabaseAsync() = CoroutineScope(Job()).async(Dispatchers.IO)  {
        try {
            return@async searchedDao.all()
        } catch (e: Exception) {
            e.printStackTrace()
            return@async e
        }
    }

    fun saveOnDatabaseAsync(categories: List<*>) = CoroutineScope(Job()).async(Dispatchers.IO) {
        try {
            categories.forEach {
                categoryDao.add(Category(it as String))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveOnDatabaseAsync(query: String) = CoroutineScope(Job()).async(Dispatchers.IO) {
        try {
            searchedDao.add(Searched(query))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}