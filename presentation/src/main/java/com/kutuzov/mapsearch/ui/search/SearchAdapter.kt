package com.kutuzov.mapsearch.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kutuzov.mapsearch.R
import com.kutuzov.mapsearch.ui.base.BaseAdapter
import com.kutuzov.mapsearch.ui.base.BaseViewHolder
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails
import kotlinx.android.synthetic.main.search_item_layout.view.*

private val comparator =
    { old: FuzzySearchDetails, new: FuzzySearchDetails -> old.hashCode() == new.hashCode() }

data class SearchItemCallbacks(
    val onSaveClick: (FuzzySearchDetails) -> Unit = {},
    val showOnMapClick: (FuzzySearchDetails) -> Unit = {}
)

class SearchAdapter(private val callback: SearchItemCallbacks) : BaseAdapter<FuzzySearchDetails> (
    areItemsSame = comparator,
    areContentsSame = comparator
        ){
    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<FuzzySearchDetails> {
        val view = layoutInflater.inflate(R.layout.search_item_layout, parent, false)
        return SearchItemViewHolder(view)
    }

    inner class SearchItemViewHolder(private val view: View) :
        BaseViewHolder<FuzzySearchDetails>(view) {

        override fun bind(data: FuzzySearchDetails, position: Int) {
            view.address.text = data.address?.freeFormAddress

            view.save_button.setOnClickListener { callback.onSaveClick.invoke(data) }
            view.showOnMap_button.setOnClickListener { callback.showOnMapClick.invoke(data) }
        }
    }
}