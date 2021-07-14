package com.kutuzov.mapsearch.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutuzov.domain.model.Data
import com.kutuzov.domain.repo.SearchRepository
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchSpecification
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get

class SearchViewModel : ViewModel(), KoinComponent {

    private val _searchResult: MutableLiveData<Data<List<FuzzySearchDetails>>> = MutableLiveData()

    val searchResult: LiveData<Data<List<FuzzySearchDetails>>>
        get() = _searchResult

    private val repository : SearchRepository = get()

    fun search(text: String) {
        val searchSpec = FuzzySearchSpecification.Builder(text).build()
        viewModelScope.launch {
            val result = repository.search(searchSpec)
            if (result.isSuccess()) {
                _searchResult.postValue(Data(content = result.value().fuzzyDetailsList))
            } else {
                _searchResult.postValue(Data(error = result.cause()))
            }
        }
    }

    fun saveToFavorites(it: FuzzySearchDetails) {
        viewModelScope.launch {
            repository.saveToFavorites(it)
        }
    }

    fun cleanResult(){
        _searchResult.postValue(null)
    }
}