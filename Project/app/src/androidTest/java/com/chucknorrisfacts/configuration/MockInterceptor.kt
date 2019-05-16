package com.chucknorrisfacts.configuration

import com.mock.ResponseJson
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.ResponseBody

class MockInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val mediaJson = MediaType.parse("application/json")
        val request = chain.request()
        val path = request.url().encodedPath()

        var code = 200
        val json = StringBuilder()
        val responseJson = ResponseJson()

        when {
            path.contains("/jokes/search") -> {
                json.append(responseJson.search())
            }
            path.contains("/jokes/categories") -> {
                json.append(responseJson.categories())
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