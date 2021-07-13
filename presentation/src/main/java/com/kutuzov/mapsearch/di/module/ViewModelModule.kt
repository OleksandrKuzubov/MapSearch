package com.kutuzov.mapsearch.di.module

import com.kutuzov.mapsearch.ui.main.MainViewModel
import com.kutuzov.mapsearch.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModule: Module = module {
    viewModel { MainViewModel() }
    viewModel { SearchViewModel() }
}