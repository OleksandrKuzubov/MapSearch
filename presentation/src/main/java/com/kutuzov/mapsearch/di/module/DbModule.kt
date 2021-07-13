package com.kutuzov.mapsearch.di.module

import android.content.Context
import com.kutuzov.data.local.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

val dbModule: Module = module {
    single { provideDatabase(get()) }
    single { provideFavoritesDao(get())}
}

fun provideDatabase(appContext: Context) = AppDatabase.getDatabase(appContext)

fun provideFavoritesDao(db: AppDatabase) = db.characterDao()
