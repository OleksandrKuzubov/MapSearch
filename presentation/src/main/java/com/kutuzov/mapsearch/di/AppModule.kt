package nl.tmg.presentation.di

import com.kutuzov.mapsearch.di.module.dbModule
import com.kutuzov.mapsearch.di.module.repositoryModule
import com.kutuzov.mapsearch.di.module.viewModelModule
import org.koin.core.context.loadKoinModules

fun appModule() = appModule

private val appModule by lazy {
    loadKoinModules(
        listOf(
            dbModule,
            repositoryModule,
            viewModelModule
        )
    )
}