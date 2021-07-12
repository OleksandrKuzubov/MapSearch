package com.kutuzov.mapsearch

import android.app.Application
import nl.tmg.presentation.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MapSearchApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MapSearchApp)
            appModule()
        }
    }
}