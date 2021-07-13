package com.kutuzov.data.repo

import com.kutuzov.data.entities.AddressDto
import com.kutuzov.data.local.FavoritesDao
import com.kutuzov.domain.model.FavoriteAddress
import com.kutuzov.domain.repo.FavoritesRepository
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails

class FavoritesRepositoryImpl(private val local: FavoritesDao) : FavoritesRepository {

    override suspend fun saveToFavorites(item: FuzzySearchDetails) {
        local.insert(AddressDto.from(item))
    }

    override suspend fun getFavorites(): List<FavoriteAddress> {
        return local.getFavorites().map { it.toFavoriteAddress() }
    }
}