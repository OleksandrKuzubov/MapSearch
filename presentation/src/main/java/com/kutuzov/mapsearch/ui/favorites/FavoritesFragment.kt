package com.kutuzov.mapsearch.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.kutuzov.mapsearch.R
import io.sulek.ssml.SSMLLinearLayoutManager
import kotlinx.android.synthetic.main.favorites_fragment.*

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by navGraphViewModels(R.id.map_search_nav_graph)

    private val favoritesAdapter : FavoritesAdapter by lazy { FavoritesAdapter(favoritesCallbacks) }

    private val favoritesCallbacks by lazy {
        FavoritesItemCallbacks(
            onItemRemoved = {
                viewModel.removeAddress(it)
                Toast.makeText(context, "${it.freeFormAddress} saved to favorites", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
        subscribeObservers()
        viewModel.getFavorites()
    }

    private fun subscribeObservers() {
        viewModel.favorites.observe(viewLifecycleOwner, Observer {
            favoritesAdapter.submitList(it)
        })
    }

    private fun initAdapter() {
        recycler_view.layoutManager = SSMLLinearLayoutManager(requireContext())
        recycler_view.adapter = favoritesAdapter
    }
}