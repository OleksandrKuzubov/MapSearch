package com.kutuzov.mapsearch.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutuzov.domain.repo.FavoritesRepository
import com.kutuzov.mapsearch.BuildConfig
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.SearchException
import com.tomtom.online.sdk.search.fuzzy.FuzzyOutcome
import com.tomtom.online.sdk.search.fuzzy.FuzzyOutcomeCallback
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchSpecification
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get

class SearchViewModel : ViewModel(), KoinComponent {

    private val searchApi = OnlineSearchApi.create(get(), BuildConfig.API_KEY)

    private val _searchResult: MutableLiveData<List<FuzzySearchDetails>> = MutableLiveData()

    val searchResult: LiveData<List<FuzzySearchDetails>>
        get() = _searchResult

    private val repository : FavoritesRepository = get()

    private val searchCallback = object : FuzzyOutcomeCallback {
        override fun onError(error: SearchException) {
            Log.e("trololo", "search error", error)
        }

        override fun onSuccess(fuzzyOutcome: FuzzyOutcome) {
            Log.d("trololo", fuzzyOutcome.summary.toString())
            _searchResult.postValue(fuzzyOutcome.fuzzyDetailsList)
        }
    }

    fun search(text: String) {
        val searchSpec = FuzzySearchSpecification.Builder(text).build()
        searchApi.search(searchSpec, searchCallback)
    }

    fun saveToFavorites(it: FuzzySearchDetails) {
        viewModelScope.launch {
            repository.saveToFavorites(it)
        }
    }
}