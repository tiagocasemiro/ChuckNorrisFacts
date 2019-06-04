package com.chucknorrisfacts.configuration

import androidx.room.Room
import com.chucknorrisfacts.BuildConfig
import com.chucknorrisfacts.controller.SearchController
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

val controllersModule = module {
    factory {
        SearchController(get<Retrofit>().create(ClientApi::class.java), get<ApplicationDatabase>().searchedDao(), get<ApplicationDatabase>().categoryDao())
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), ApplicationDatabase::class.java, BuildConfig.DATABASE)
            .allowMainThreadQueries()
            .build()
    }
    single {
        get<ApplicationDatabase>().categoryDao()
    }
    single {
        get<ApplicationDatabase>().searchedDao()
    }
}

val clientApiModule = module {
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
                    .build())
            .build()
    }
    factory {
        get<Retrofit>().create(ClientApi::class.java)
    }
}
