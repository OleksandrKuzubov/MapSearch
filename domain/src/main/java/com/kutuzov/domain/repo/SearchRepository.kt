package com.kutuzov.domain.repo

import com.kutuzov.domain.model.FavoriteAddress
import com.tomtom.online.sdk.common.Result
import com.tomtom.online.sdk.search.fuzzy.FuzzyOutcome
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchSpecification

interface SearchRepository {

    suspend fun saveToFavorites(item : FuzzySearchDetails)

    suspend fun getFavorites(): List<FavoriteAddress>

    suspend fun search(searchSpec: FuzzySearchSpecification): Result<FuzzyOutcome>

    suspend fun removeAddressById(id: String)
}