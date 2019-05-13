package com.example.mock

import com.squareup.okhttp.internal.Internal.logger
import com.squareup.okhttp.mockwebserver.MockWebServer
import com.squareup.okhttp.mockwebserver.Dispatcher
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.RecordedRequest
import java.io.IOException
import java.net.InetAddress

class MockServer() {
    var mockServer = MockWebServer()

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest?): MockResponse {
            val mockResponse = MockResponse()
            val json = ResponseJson()

            mockResponse
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")

                when(request!!.path!!) {
                    "/jokes/search" -> {
                        mockResponse.setBody(json.search())
                        mockResponse.setResponseCode(200)
                    }
                    "/jokes/categories" -> {
                        mockResponse.setBody(json.categories())
                        mockResponse.setResponseCode(200)
                    }
                    else -> {
                        mockResponse.setResponseCode(404)
                    }
                }

            return mockResponse
        }
    }

    fun start() {
        try {
            mockServer.setDispatcher(dispatcher)
            mockServer.start(InetAddress.getByName(InetAddress.getLocalHost().hostAddress), 8080)
            logger.info("http://" + InetAddress.getLocalHost().hostAddress + ":8080")
        }catch (e: IOException) {
            logger.info("Erro ao tentar iniciar servidor  -->  "+ e.message + e.cause.toString())
        }
    }

    fun stop() {
        mockServer.shutdown()
    }
}