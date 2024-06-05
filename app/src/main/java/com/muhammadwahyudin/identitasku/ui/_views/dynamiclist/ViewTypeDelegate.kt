package com.muhammadwahyudin.identitasku.ui._views.dynamiclist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ViewTypeDelegate {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DynamicItem)
    fun isForViewType(item: DynamicItem): Boolean

}