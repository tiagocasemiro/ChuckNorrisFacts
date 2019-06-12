package com.chucknorrisfacts.controller

import com.chucknorrisfacts.configuration.addOverridingIfExists
import com.chucknorrisfacts.configuration.shuffledAndSlice
import com.chucknorrisfacts.model.service.SearchService
import com.domain.Category
import com.domain.Search
import com.domain.Searched
import kotlinx.coroutines.*


class SearchController(private val searchService: SearchService) {

    fun categories(success: (categories: List<Category>) -> Unit, fail: () -> Unit) = GlobalScope.launch {
        searchService.categoriesFromDatabaseAsync().await().let { categoriesFromDb ->
            if (categoriesFromDb is List<*> && categoriesFromDb.isNotEmpty()) {
                success((categoriesFromDb).map { category -> category as Category }.toList().shuffledAndSlice())
            } else {
                searchService.categoriesFromRemoteApiAsync().await().let { categoriesFromApi ->
                    if (categoriesFromApi is List<*> && categoriesFromApi.isNotEmpty()) {
                        searchService.saveOnDatabaseAsync(categoriesFromApi.toList()).start()
                        success(categoriesFromApi.map { Category(it as String) }.toList().shuffledAndSlice())
                    } else  {
                        fail()
                    }
                }
            }
        }
    }

    fun searchWith(query: String, success: (search: Search) -> Unit, fail: () -> Unit) = GlobalScope.launch {
        when (val objectReturned = searchService.searchWithQueryFromRemoteApiAsync(query).await()) {
            is Search -> {
                success(objectReturned)
            } else -> {
                fail()
            }
        }

        searchService.saveOnDatabaseAsync(query).start()
    }

    fun searchWith(query: String, success: (search: Search) -> Unit, fail: () -> Unit,
                   successSearcheds : (searcheds: List<Searched>) -> Unit) = GlobalScope.launch {
        when (val objectReturned = searchService.searchedsFromDatabaseAsync().await()) {
            is List<*> -> {
                val searcheds = objectReturned.map { it as Searched }.toMutableList()
                searcheds.addOverridingIfExists(Searched(query))
                successSearcheds(searcheds.reversed())
            } else -> {
                val searcheds = mutableListOf<Searched>()
                searcheds.add(Searched(query))
                successSearcheds(searcheds)
            }
        }

        this@SearchController.searchWith(query, success, fail)
    }

    fun searcheds(success: (facts: List<Searched>) -> Unit, fail: () -> Unit) = GlobalScope.launch {
        when (val objectReturned = searchService.searchedsFromDatabaseAsync().await()) {
            is List<*> -> {
                success(objectReturned.map { it as Searched }.toList().reversed())
            } else -> {
                fail()
            }
        }
    }
}
