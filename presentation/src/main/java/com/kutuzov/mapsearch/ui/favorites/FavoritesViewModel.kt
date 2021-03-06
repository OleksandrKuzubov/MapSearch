package com.kutuzov.mapsearch.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutuzov.domain.model.FavoriteAddress
import com.kutuzov.domain.repo.SearchRepository
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get

class FavoritesViewModel : ViewModel(), KoinComponent {

    fun removeAddress(it: FavoriteAddress) {
        viewModelScope.launch {
            repository.removeAddressById(it.id)

            getFavorites()
        }
    }

    private var repository: SearchRepository = get()

    private val _favorites: MutableLiveData<List<FavoriteAddress>> = MutableLiveData()

    val favorites: LiveData<List<FavoriteAddress>>
        get() = _favorites

    fun getFavorites() {
        viewModelScope.launch {
            _favorites.postValue(repository.getFavorites())
        }
    }
}