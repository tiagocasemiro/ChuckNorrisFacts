package com.chucknorrisfacts.configuration

import android.app.Application
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin
import android.R
import android.graphics.Color
import androidx.emoji.bundled.BundledEmojiCompatConfig


class ChuckNorrisApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        startKoin(this, listOf(controllersModule, databaseModule, clientApiModule))
        EmojiCompat.init(BundledEmojiCompatConfig(this))
    }
}