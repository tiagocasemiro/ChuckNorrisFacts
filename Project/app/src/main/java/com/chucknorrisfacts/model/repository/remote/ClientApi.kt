package com.chucknorrisfacts.model.repository.remote

import com.domain.Search
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ClientApi {
    @GET("https://api.chucknorris.io/jokes/search")
    fun search(@Query("query") query: String): Call<Search>

    @GET("https://api.chucknorris.io/jokes/categories")
    fun categories(): Call<List<String>>
}