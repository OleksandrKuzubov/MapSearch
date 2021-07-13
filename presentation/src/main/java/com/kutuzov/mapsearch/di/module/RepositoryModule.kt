package com.kutuzov.mapsearch.di.module

import com.kutuzov.data.local.FavoritesDao
import com.kutuzov.data.repo.FavoritesRepositoryImpl
import com.kutuzov.domain.repo.FavoritesRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    single { provideFavoritesAddressRepository(get()) }
}

fun provideFavoritesAddressRepository(localSource: FavoritesDao): FavoritesRepository {
    return FavoritesRepositoryImpl(localSource)
}