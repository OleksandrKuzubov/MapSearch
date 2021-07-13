package com.kutuzov.domain.repo

import com.kutuzov.domain.model.FavoriteAddress
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails

interface FavoritesRepository {

    suspend fun saveToFavorites(item : FuzzySearchDetails)

    suspend fun getFavorites(): List<FavoriteAddress>

}