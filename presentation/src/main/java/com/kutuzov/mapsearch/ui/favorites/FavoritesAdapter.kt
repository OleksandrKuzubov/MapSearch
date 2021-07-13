package com.kutuzov.mapsearch.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kutuzov.domain.model.FavoriteAddress
import com.kutuzov.mapsearch.R
import com.kutuzov.mapsearch.ui.base.BaseAdapter
import com.kutuzov.mapsearch.ui.base.BaseViewHolder
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails
import kotlinx.android.synthetic.main.search_item_layout.view.*

private val comparator =
    { old: FavoriteAddress, new: FavoriteAddress -> old.hashCode() == new.hashCode() }

data class FavoritesItemCallbacks(
    val onItemRemoved: (FavoriteAddress) -> Unit = {}
)

class FavoritesAdapter(private val callback: FavoritesItemCallbacks?) : BaseAdapter<FavoriteAddress>(
    areItemsSame = comparator,
    areContentsSame = comparator
){
    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<FavoriteAddress> {
        val view = layoutInflater.inflate(R.layout.search_item_layout, parent, false)
        return FavoriteItemViewHolder(view)
    }

    inner class FavoriteItemViewHolder(private val view: View) :
        BaseViewHolder<FavoriteAddress>(view) {

        override fun bind(data: FavoriteAddress, position: Int) {
            view.address.text = data.freeFormAddress

//            view.setOnClickListener { callback.onItemClick.invoke(data) }
        }
    }
}