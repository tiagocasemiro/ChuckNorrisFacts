package com.chucknorrisfacts.configuration

import com.chucknorrisfacts.mockJsonSearchFromApi
import com.chucknorrisfacts.mockJsonCategoryFromApi
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.ResponseBody

class MockInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val mediaJson = MediaType.parse("application/json")
        val path = chain.request().url().encodedPath()
        var code = 200
        val json = StringBuilder()

        when {
            path.contains("/jokes/search") -> {
                json.append(mockJsonSearchFromApi())
            }
            path.contains("/jokes/categories") -> {
                json.append(mockJsonCategoryFromApi())
            }
            else -> code = 404
        }

        return okhttp3.Response.Builder()
            .body(ResponseBody.create(mediaJson, json.toString()))
            .request(chain.request())
            .protocol(Protocol.HTTP_2)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .message(String())
            .code(code)
            .build()
    }
}