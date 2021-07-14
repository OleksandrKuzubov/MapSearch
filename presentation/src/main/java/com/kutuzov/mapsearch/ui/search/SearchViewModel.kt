package com.kutuzov.mapsearch.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutuzov.domain.repo.SearchRepository
import com.tomtom.online.sdk.search.SearchException
import com.tomtom.online.sdk.search.fuzzy.FuzzyOutcome
import com.tomtom.online.sdk.search.fuzzy.FuzzyOutcomeCallback
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchSpecification
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get

class SearchViewModel : ViewModel(), KoinComponent {

    private val _searchResult: MutableLiveData<List<FuzzySearchDetails>> = MutableLiveData()

    val searchResult: LiveData<List<FuzzySearchDetails>>
        get() = _searchResult

    private val repository : SearchRepository = get()

    fun search(text: String) {
        val searchSpec = FuzzySearchSpecification.Builder(text).build()
        viewModelScope.launch {
            val result = repository.search(searchSpec)
            if (result.isSuccess()) {
                _searchResult.postValue(result.value().fuzzyDetailsList)
            }
        }
    }

    fun saveToFavorites(it: FuzzySearchDetails) {
        viewModelScope.launch {
            repository.saveToFavorites(it)
        }
    }
}