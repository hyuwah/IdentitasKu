package com.muhammadwahyudin.identitasku.ui.home

import android.content.ClipData
import android.content.ClipboardManager
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
import org.jetbrains.anko.toast
import timber.log.Timber

class DataAdapter(val ctx: Context) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    var datasWithType: List<DataWithDataType> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
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
            Constants.TYPE_KTP -> Constants.TYPE_KTP
            else -> Constants.TYPE_DEFAULT
        }
    }

    override fun getItemCount(): Int {
        return datasWithType.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ctx, datasWithType[position])
        holder.itemView.setOnClickListener {
            Timber.d("Data ${datasWithType[position]}")
        }
        holder.itemView.setOnLongClickListener {
            ctx.alert("Yakin mau di delete?", "Wait!") {
                positiveButton("Ya") {
                    Timber.d("Delete")
                    (ctx as HomeActivity).viewModel.deleteData(datasWithType[position])
                }
                negativeButton("Nggak") {}
                show()
            }
            true
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(ctx: Context, dataWithDataType: DataWithDataType) {
            when (dataWithDataType.typeId) {
                Constants.TYPE_KTP -> {
                    itemView.tv_data_type_ktp.text = dataWithDataType.typeName
                    itemView.tv_data_value_ktp.text = dataWithDataType.value
                    itemView.btn_copy_value_ktp.setOnClickListener {
                        copyToClipboard(ctx, dataWithDataType.value)
                    }
                }
                else -> {
                    itemView.tv_data_type.text = dataWithDataType.typeName
                    itemView.tv_data_value.text = dataWithDataType.value
                    itemView.btn_copy_value.setOnClickListener {
                        copyToClipboard(ctx, dataWithDataType.value)
                    }
                }
            }
        }

        private fun copyToClipboard(ctx: Context, text: String) {
            val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText(ctx.packageName, text)
            clipboard.primaryClip = clip
            ctx.toast("Copied to clipboard")
        }
    }

}