package com.kutuzov.mapsearch.di.module

import android.content.Context
import com.kutuzov.mapsearch.BuildConfig
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.SearchApi
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: Module = module {
    single { provideSearchService(get()) }
}

fun provideSearchService(context: Context): SearchApi = OnlineSearchApi.create(context, BuildConfig.API_KEY)

