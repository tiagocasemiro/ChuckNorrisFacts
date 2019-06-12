package com.chucknorrisfacts.configuration

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin


@Suppress("unused")
class ChuckNorrisApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        startKoin(this, listOf(controllersModule, serviceModule, databaseModule, clientApiModule))
        EmojiCompat.init(BundledEmojiCompatConfig(this))
    }
}