package com.chucknorrisfacts.model.repository.remote

import com.domain.Category
import com.domain.Fact
import retrofit2.Call
import retrofit2.http.*

interface ClientApi {
    @GET("https://api.chucknorris.io/jokes/search?query={query}")
    fun search(@Path("query") query: String): Call<List<Fact>>

    @GET("https://api.chucknorris.io/jokes/categories")
    fun categories(): Call<List<String>>
}