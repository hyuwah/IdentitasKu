package com.muhammadwahyudin.identitasku.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import kotlinx.android.synthetic.main.item_home_data_list.view.*
import kotlinx.android.synthetic.main.item_home_data_list_ktp.view.*
import org.jetbrains.anko.alert
import timber.log.Timber

class DataAdapter(val ctx: Context) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    var datasWithType: List<DataWithDataType> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Constants.TYPE_KTP -> ViewHolder(
                LayoutInflater.from(ctx).inflate(
                    R.layout.item_home_data_list_ktp,
                    parent,
                    false
                )
            )
            else -> ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_home_data_list, parent, false))

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (datasWithType[position].typeId) {
            Constants.TYPE_KTP -> {
                Constants.TYPE_KTP
            }
            else -> {
                Constants.TYPE_DEFAULT
            }
        }
    }

    override fun getItemCount(): Int {
        return datasWithType.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datasWithType[position])
        holder.itemView.setOnClickListener {
            ctx.alert("Yakin mau di delete?", "Wait!") {
                positiveButton("Ya") {
                    Timber.d("Delete")
                    (ctx as HomeActivity).viewModel.deleteData(datasWithType[position])
                }
                negativeButton("Nggak") {}
                show()
            }
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dataWithDataType: DataWithDataType) {
            when (dataWithDataType.typeId) {
                Constants.TYPE_KTP -> {
                    itemView.tv_data_type_ktp.text = dataWithDataType.typeName
                    itemView.tv_data_value_ktp.text = dataWithDataType.value
                }
                else -> {
                    itemView.tv_data_type.text = dataWithDataType.typeName
                    itemView.tv_data_value.text = dataWithDataType.value
                }
            }
        }
    }

}