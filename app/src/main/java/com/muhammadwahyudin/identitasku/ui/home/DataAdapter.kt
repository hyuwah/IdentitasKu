package com.muhammadwahyudin.identitasku.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.utils.Commons
import kotlinx.android.synthetic.main.item_home_data_list.view.*
import kotlinx.android.synthetic.main.item_home_data_list_ktp.view.*
import kotlinx.android.synthetic.main.item_home_data_list_rek_bank.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import timber.log.Timber

class DataAdapter(val ctx: Context) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    var datasWithType: List<DataWithDataType> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Constants.TYPE_KTP -> createViewHolder(R.layout.item_home_data_list_ktp, parent)
            Constants.TYPE_REK_BANK -> createViewHolder(R.layout.item_home_data_list_rek_bank, parent)
            else -> createViewHolder(R.layout.item_home_data_list, parent)
        }
    }

    private fun createViewHolder(layoutId: Int, parent: ViewGroup): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(layoutId, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return when (datasWithType[position].typeId) {
            Constants.TYPE_KTP,
            Constants.TYPE_HANDPHONE,
            Constants.TYPE_ALAMAT,
            Constants.TYPE_PLN,
            Constants.TYPE_PDAM,
            Constants.TYPE_NPWP,
            Constants.TYPE_REK_BANK,
            Constants.TYPE_KK,
            Constants.TYPE_STNK,
            Constants.TYPE_CC,
            Constants.TYPE_BPJS,
            Constants.TYPE_EMAIL -> datasWithType[position].typeId
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
            val bs = AddEditDataBottomSheet.newInstance(AddEditDataBottomSheet.EDIT, datasWithType[position])
            bs.show((ctx as HomeActivity).supportFragmentManager, bs.tag)
        }
        holder.itemView.setOnLongClickListener {
            ctx.alert(Appcompat, "Yakin mau di delete?", "Wait!") {
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
                        Commons.copyToClipboard(ctx, dataWithDataType.typeName)
                    }
                }
                Constants.TYPE_REK_BANK -> {
                    itemView.tv_data_bank.text = dataWithDataType.attr1
                    itemView.tv_data_value_rek_bank.text = dataWithDataType.value
                    itemView.btn_copy_value_rek_bank.setOnClickListener {
                        Commons.copyToClipboard(ctx, dataWithDataType.typeName)
                    }
                }
                else -> {
                    itemView.tv_data_type.text = dataWithDataType.typeName
                    itemView.tv_data_value.text = dataWithDataType.value
                    itemView.btn_copy_value.setOnClickListener {
                        Commons.copyToClipboard(ctx, dataWithDataType.typeName)
                    }
                }
            }
        }

    }

}