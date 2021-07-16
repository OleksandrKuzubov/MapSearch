package com.kutuzov.data.repo

import com.kutuzov.data.entities.AddressDto
import com.kutuzov.data.local.FavoritesDao
import com.kutuzov.domain.model.FavoriteAddress
import com.kutuzov.domain.repo.SearchRepository
import com.tomtom.online.sdk.common.Result
import com.tomtom.online.sdk.search.SearchApi
import com.tomtom.online.sdk.search.fuzzy.FuzzyOutcome
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchSpecification

class SearchRepositoryImpl(private val local: FavoritesDao, private val remote: SearchApi) : SearchRepository {

    override suspend fun saveToFavorites(item: FuzzySearchDetails) {
        local.insert(AddressDto.from(item))
    }

    override suspend fun getFavorites(): List<FavoriteAddress> {
        return local.getFavorites().map { it.toFavoriteAddress() }
    }

    override suspend fun search(searchSpec: FuzzySearchSpecification): Result<FuzzyOutcome> {
        return remote.search(searchSpec)
    }

    override suspend fun removeAddressById(id: String) {
        local.removeAddressById(id)
    }
}