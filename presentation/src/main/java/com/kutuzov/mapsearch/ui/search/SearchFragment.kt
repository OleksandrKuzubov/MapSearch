package com.kutuzov.mapsearch.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.kutuzov.mapsearch.R
import com.kutuzov.mapsearch.ui.main.MainFragment
import com.kutuzov.mapsearch.utils.DebouncingQueryTextListener
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails
import io.sulek.ssml.SSMLLinearLayoutManager
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    companion object {
        private const val SEARCH_LIMIT = 2
    }

    private val viewModel: SearchViewModel by navGraphViewModels(R.id.map_search_nav_graph)
    private val searchAdapter: SearchAdapter by lazy { SearchAdapter(searchSectionItemCallbacks) }

    private val queryTextListener: SearchView.OnQueryTextListener by lazy {
        DebouncingQueryTextListener(
            viewLifecycleOwner
        ) { newText ->
            newText?.let {
                if (it.length > SEARCH_LIMIT) {
                    viewModel.search(it)
                }
            }
        }
    }

    private val searchSectionItemCallbacks by lazy {
        SearchItemCallbacks(
            onSaveClick = {
                viewModel.saveToFavorites(it)
                Toast.makeText(
                    context,
                    "${it.address?.freeFormAddress} saved to favorites",
                    Toast.LENGTH_SHORT
                ).show()
            },
            showOnMapClick = {
                showOnMap(it)
            }
        )
    }

    private fun showOnMap(it: FuzzySearchDetails) {
        Toast.makeText(context, "Show on Map", Toast.LENGTH_SHORT).show()
        navigateBackToFragmentA(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initSearch()
        initAdapter()
        subscribeObservers()
    }

    private fun initAdapter() {
        recycler_view.layoutManager = SSMLLinearLayoutManager(requireContext())
        recycler_view.adapter = searchAdapter
    }

    private fun initSearch() {
        search_view.apply {
            setOnQueryTextListener(queryTextListener)
        }
    }

    private fun subscribeObservers() {
        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            searchAdapter.submitList(it)
        })
    }

    private fun navigateBackToFragmentA(item: FuzzySearchDetails) {
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(MainFragment.SEARCH_RESULT, item)
        findNavController().navigateUp()
    }
}