package com.kutuzov.mapsearch.ui.search

import android.os.Bundle
import android.view.*
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

    private lateinit var showOnMapView: MenuItem

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
        goToMapWithResult(listOf(it))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
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
            it?.let { data ->
                if (data.error != null) {
                    Toast.makeText(context, it.error.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    data.content?.let { content ->
                        empty_view.visibility = if (content.isNullOrEmpty()) View.VISIBLE else View.GONE
                        showOnMapView.isVisible = !content.isNullOrEmpty()
                        searchAdapter.submitList(content)
                    }
                }
            }
        })
    }

    private fun goToMapWithResult(data: List<FuzzySearchDetails>) {
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(MainFragment.SEARCH_RESULT, data)
        findNavController().navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_fragment_menu, menu)
        showOnMapView = menu.findItem(R.id.show_on_map)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                viewModel.cleanResult()
                super.onOptionsItemSelected(item)
            }
            R.id.show_on_map -> {
                viewModel.searchResult.value?.content?.let { goToMapWithResult(it) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}