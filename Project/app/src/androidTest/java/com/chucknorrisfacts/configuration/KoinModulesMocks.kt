package com.chucknorrisfacts.configuration

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.chucknorrisfacts.BuildConfig
import com.chucknorrisfacts.controller.SearchController
import com.chucknorrisfacts.model.repository.local.ApplicationDatabase
import com.chucknorrisfacts.model.repository.remote.ClientApi
import com.chucknorrisfacts.model.repository.remote.FactDeserialize
import com.chucknorrisfacts.model.service.SearchService
import com.domain.Fact
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

val controllersModule = module {
    factory {
        SearchController(get())
    }
}

val serviceModule = module {
    factory {
        SearchService(get<Retrofit>().create(ClientApi::class.java), get<ApplicationDatabase>().searchedDao(), get<ApplicationDatabase>().categoryDao())
    }
}

val clientApiModuleMock = module(override = true) {
    factory {
        Retrofit.Builder()
            .baseUrl(BuildConfig.HOST)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().registerTypeAdapter(Fact::class.java, FactDeserialize()).create()))
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                        message -> Logger.getLogger("CHUCK NORRIS TEST LOG -> ").info(message)
                    }).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(get<MockInterceptor>())
                    .build())
            .build()
    }
    factory {
        get<Retrofit>().create(ClientApi::class.java)
    }
    single {
        MockInterceptor()
    }
}

val databaseModuleMock = module(override = true) {
    single {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), ApplicationDatabase::class.java).build()
    }
    single {
        get<ApplicationDatabase>().searchedDao()
    }
    single {
        get<ApplicationDatabase>().categoryDao()
    }
}