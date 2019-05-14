package com.chucknorrisfacts.controller

import com.chucknorrisfacts.model.repository.exception.NoResultException
import com.chucknorrisfacts.model.repository.local.CategoryDao
import com.chucknorrisfacts.model.repository.local.SearchedDao
import com.chucknorrisfacts.model.repository.remote.ClientApi
import com.domain.Category
import com.domain.Searched
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class SearchController(private val clientApi: ClientApi, private val searchedDao: SearchedDao, private val categoryDao: CategoryDao) {
    fun categories() = GlobalScope.async(Dispatchers.IO) {
        try {
            val response = clientApi.categories().execute()
            if (response.isSuccessful) {
                save(response.body()!!)
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

    fun searchWith(query: String)  = GlobalScope.async(Dispatchers.IO) {
        try {
            val response = clientApi.search(query).execute()
            save(query)
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

    private fun save(categories: List<Category>) = GlobalScope.async(Dispatchers.IO) {
        try {
            categories.forEach {
                categoryDao.add(it)
            }
        } catch (e: Exception) {
           e.printStackTrace()
        }
    }

    private fun save(query: String) = GlobalScope.async(Dispatchers.IO) {
        try {
            searchedDao.add(Searched(query))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun allCategories() : List<Category> {
        return try {
            categoryDao.all()
        } catch (e: Exception) {
            e.printStackTrace()

            emptyList()
        }
    }

    private fun allSearched() : List<Searched> {
        return try {
            searchedDao.all()
        } catch (e: Exception) {
            e.printStackTrace()

            emptyList()
        }
    }
}