package com.muhammadwahyudin.identitasku.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import kotlinx.android.synthetic.main.item_home_data_list.view.*

class DataAdapter(val ctx: Context) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    var datasWithType: List<DataWithDataType> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_home_data_list, parent, false))
    }

    override fun getItemCount(): Int {
        return datasWithType.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datasWithType[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataWithDataType: DataWithDataType) {
            itemView.tv_data_type.text = dataWithDataType.typeName
            itemView.tv_data_value.text = dataWithDataType.value
        }
    }

}