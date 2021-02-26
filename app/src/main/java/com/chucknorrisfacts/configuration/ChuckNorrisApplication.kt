package com.chucknorrisfacts.configuration

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import org.koin.android.ext.android.startKoin


@Suppress("unused")
class ChuckNorrisApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(controllersModule, serviceModule, databaseModule, clientApiModule))
        EmojiCompat.init(BundledEmojiCompatConfig(this))
    }
}