package com.muhammadwahyudin.identitasku.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataType
import kotlinx.android.synthetic.main.item_home_data_list.view.*

class DataAdapter(val ctx: Context) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    var datas: List<Data> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var dataTypes: List<DataType> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_home_data_list, parent, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var dataTypeName = dataTypes.find { it.id == datas[position].typeId }?.name
        holder.tv_data_type.text = dataTypeName
        holder.tv_data_value.text = datas[position].value
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_data_type = itemView.tv_data_type
        val tv_data_value = itemView.tv_data_value
    }

}