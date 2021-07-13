package com.kutuzov.mapsearch.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutuzov.domain.model.FavoriteAddress
import com.kutuzov.domain.repo.FavoritesRepository
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get

class FavoritesViewModel : ViewModel(), KoinComponent {

    fun removeAddress(it: FavoriteAddress) {

    }

    private var repository: FavoritesRepository = get()

    private val _favorites: MutableLiveData<List<FavoriteAddress>> = MutableLiveData()

    val favorites: LiveData<List<FavoriteAddress>>
        get() = _favorites

    init {
        getFavorites()
    }

    private fun getFavorites() {
        viewModelScope.launch {
            _favorites.postValue(repository.getFavorites())
        }
    }
}