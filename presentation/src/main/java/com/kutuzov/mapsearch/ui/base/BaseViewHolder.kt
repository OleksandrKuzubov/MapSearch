package com.kutuzov.mapsearch.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<in T>(view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun bind(data: T, position: Int)
}