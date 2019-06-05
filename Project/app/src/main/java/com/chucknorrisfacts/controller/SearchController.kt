package com.chucknorrisfacts.controller

import com.chucknorrisfacts.configuration.addOverridingIfExists
import com.chucknorrisfacts.model.service.SearchService
import com.domain.Category
import com.domain.Search
import com.domain.Searched
import kotlinx.coroutines.*


class SearchController(private val searchService: SearchService) {

    fun categories(success: (categories: List<Category>) -> Unit, fail: () -> Unit) = GlobalScope.launch {
        if(GlobalScope.async {
            withContext(Dispatchers.Main) {
                searchService.categoriesFromDatabaseAsync().await().let {
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
                        return@withContext true
                    }
                }
            }
        }.await()) {
            withContext(Dispatchers.Main) {
                when (val objectReturned = searchService.categoriesFromRemoteApiAsync().await()) {
                    is List<*> -> {
                        searchService.saveOnDatabaseAsync(objectReturned).start()
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
            when (val objectReturned = searchService.searchWithQueryFromRemoteApiAsync(query).await()) {
                is Search -> {
                    success(objectReturned)
                } else -> {
                    fail()
                }
            }
        }
        searchService.saveOnDatabaseAsync(query).start()
    }

    fun searchWith(query: String, success: (search: Search) -> Unit, fail: () -> Unit,
                   successSearcheds : (searcheds: List<Searched>) -> Unit) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            when (val objectReturned = searchService.searchedsFromDatabaseAsync().await()) {
                is List<*> -> {
                    val searcheds = objectReturned.map { it as Searched }.toMutableList()
                    searcheds.addOverridingIfExists(Searched(query))
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
            when (val objectReturned = searchService.searchedsFromDatabaseAsync().await()) {
                is List<*> -> {
                    success(objectReturned.map { it as Searched }.toList())
                } else -> {
                    fail()
                }
            }
        }
    }
}

