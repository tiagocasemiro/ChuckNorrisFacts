package com.chucknorrisfacts.configuration

import android.arch.persistence.room.Room
import cielo.sdk.info.InfoManager
import com.chucknorrisfacts.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val controllersModule = module {
    factory {

    }
}

val databaseModule = module {
    single {
       /* Room.databaseBuilder(get(), ApplicationDatabase::class.java, BuildConfig.DATABASE)
            .allowMainThreadQueries()
            .build()*/
    }
    single {
       // get<ApplicationDatabase>().paymentDao()
    }
    single {
        //get<ApplicationDatabase>().cancellationDao()
    }
}

val clientApiModule = module {
    factory {
        Retrofit.Builder()
            .baseUrl(BuildConfig.HOST)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                        .create()))
            .client(
                OkHttpClient.Builder()
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .writeTimeout(5000, TimeUnit.MILLISECONDS)
                    .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                            message -> "CHUCK NORRIS LOG -> $message"
                    }).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build())
            .build()
    }
    factory {
       // get<Retrofit>().create(ClientApi::class.java)
    }
}
