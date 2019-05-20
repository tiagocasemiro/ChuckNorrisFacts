package com.chucknorrisfacts.configuration

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.chucknorrisfacts.BuildConfig
import com.chucknorrisfacts.model.repository.local.ApplicationDatabase
import com.chucknorrisfacts.model.repository.remote.ClientApi
import com.chucknorrisfacts.model.repository.remote.FactDeserialize
import com.domain.Fact
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val clientApiModuleMock = module(override = true) {
    factory {
        Retrofit.Builder()
            .baseUrl(BuildConfig.HOST)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().registerTypeAdapter(Fact::class.java, FactDeserialize()).create()))
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                            message -> "CHUCK NORRIS LOG -> $message"
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