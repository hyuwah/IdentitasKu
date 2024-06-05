package com.muhammadwahyudin.identitasku.ui._views.dynamiclist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.identitasku.databinding.ItemHomeDataListGeneric1Binding
import com.muhammadwahyudin.identitasku.ui.model.TypeAModel

class TypeAViewTypeDelegate: ViewTypeDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(ItemHomeDataListGeneric1Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DynamicItem) {
        (holder as ViewHolder).bind(item as TypeAModel)
    }

    override fun isForViewType(item: DynamicItem): Boolean {
        return item is TypeAModel
    }

    class ViewHolder(private val binding: ItemHomeDataListGeneric1Binding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TypeAModel) {
            with(binding) {
                tvDataType.text = "Type"
                tvDataValue.text = "Value"
            }
        }
    }
}