package com.chucknorrisfacts.configuration

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin

class ChuckNorrisApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        startKoin(this, listOf(controllersModule, databaseModule, clientApiModule))
    }
}