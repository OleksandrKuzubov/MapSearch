package com.kutuzov.mapsearch.di.module

import com.kutuzov.data.local.FavoritesDao
import com.kutuzov.data.repo.SearchRepositoryImpl
import com.kutuzov.domain.repo.SearchRepository
import com.tomtom.online.sdk.search.SearchApi
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    single { provideFavoritesAddressRepository(get(), get()) }
}

fun provideFavoritesAddressRepository(localSource: FavoritesDao, searchService: SearchApi): SearchRepository {
    return SearchRepositoryImpl(localSource, searchService)
}