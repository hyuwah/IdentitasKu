package com.muhammadwahyudin.identitasku.ui._views.dynamiclist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


interface DynamicItem {
    val id: Int
}

class DynamicAdapter(
    private val items: List<DynamicItem>,
    private val delegates: List<ViewTypeDelegate>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = delegates.indexOfFirst { it.isForViewType(items[position]) }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        delegates[holder.itemViewType].onBindViewHolder(holder, item)
    }
}

class DiffItemCallback: DiffUtil.ItemCallback<DynamicItem>() {

    override fun areItemsTheSame(oldItem: DynamicItem, newItem: DynamicItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: DynamicItem, newItem: DynamicItem): Boolean {
        return oldItem == newItem
    }

}