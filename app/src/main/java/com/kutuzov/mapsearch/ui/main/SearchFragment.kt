package com.kutuzov.mapsearch.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.kutuzov.mapsearch.R
import com.kutuzov.mapsearch.utils.showKeyboard
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()

        private const val SEARCH_LIMIT = 3
    }

    private lateinit var viewModel: SearchViewModel

    private val queryTextListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextChange(newText: String): Boolean {
            return if (newText.length > SEARCH_LIMIT) {
                viewModel.search(newText)
                true
            } else false
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            return false
        }
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
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel

        initSearch()
    }

    private fun initSearch() {
        search_view.apply {
            setOnQueryTextListener(queryTextListener)
        }
    }
}