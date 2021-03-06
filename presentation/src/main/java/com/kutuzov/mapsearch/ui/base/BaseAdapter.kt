package com.kutuzov.mapsearch.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

typealias DiffComparator<T> = (old: T, new: T) -> Boolean

/**
 * Based class for for standard list adapters.
 *
 * To override [DiffUtil] behavior, pass diffItemsSame and diffContentsSame functions.
 */
abstract class BaseAdapter<T>(
    areItemsSame: DiffComparator<T>,
    areContentsSame: DiffComparator<T>
) : ListAdapter<T, BaseViewHolder<T>>(
    object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            areItemsSame(oldItem, newItem)

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            areContentsSame(oldItem, newItem)
    }
) {

    abstract fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> =
        createViewHolder(LayoutInflater.from(parent.context), parent, viewType)

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item, position)
        }
    }
}