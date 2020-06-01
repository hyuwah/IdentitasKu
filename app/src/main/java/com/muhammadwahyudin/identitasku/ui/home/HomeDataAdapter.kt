package com.muhammadwahyudin.identitasku.ui.home

import android.graphics.Color
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.HomeDataAdapter.Companion.LAYOUT.DEFAULT
import com.muhammadwahyudin.identitasku.utils.Commons
import com.muhammadwahyudin.identitasku.utils.setSafeOnClickListener
import com.muhammadwahyudin.identitasku.utils.setVisibleIf
import com.muhammadwahyudin.identitasku.utils.showIcons

class HomeDataAdapter(data: MutableList<DataWithDataType>) :
    BaseMultiItemQuickAdapter<DataWithDataType, BaseViewHolder>(data) {

    companion object {
        object LAYOUT {
            const val DEFAULT = 0
        }

        fun diffCallback() = object : DiffUtil.ItemCallback<DataWithDataType>() {
            override fun areItemsTheSame(
                oldItem: DataWithDataType,
                newItem: DataWithDataType
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DataWithDataType,
                newItem: DataWithDataType
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    var popupMenuListener: PopupMenuListener? = null

    init {
        addItemType(DEFAULT, R.layout.item_home_data_list_generic_3)
    }

    override fun getItemViewType(position: Int) = DEFAULT

    override fun convert(holder: BaseViewHolder, item: DataWithDataType) {
        // item.typeName doesn't respect Locale languange!
        holder.setText(R.id.tv_data_type, item.typeName)
        holder.setText(R.id.tv_data_value, item.value)
        holder.setImageResource(R.id.iv_icon, item.type().iconRes)
        holder.getView<ImageButton>(R.id.btn_copy_value).setSafeOnClickListener {
            Commons.copyToClipboard(context, item.value, item.typeName)
        }
        val popupMenu = setupPopupMenu(holder, item)
        holder.getView<ImageButton>(R.id.btn_more).setOnClickListener {
            popupMenu.show()
        }
        val hasAttr1 = !item.attr1.isNullOrEmpty()
        holder.getView<TextView>(R.id.tv_data_keterangan).setVisibleIf(hasAttr1)
        if (hasAttr1) {
            holder.setText(R.id.tv_data_keterangan, item.attr1)
        }
        val hasAttr2 = !item.attr2.isNullOrEmpty()
        holder.getView<TextView>(R.id.tv_data_attr2).setVisibleIf(hasAttr2, true)
        if (hasAttr2) {
            holder.getView<TextView>(R.id.tv_data_attr2).apply {
                isSelected = true
                text = item.attr2
                setTextColor(Color.BLACK)
            }
        }
    }

    private fun setupPopupMenu(holder: BaseViewHolder, item: DataWithDataType): PopupMenu {
        return PopupMenu(context, holder.getView(R.id.btn_more)).apply {
            inflate(R.menu.home_item_action_menu)
            showIcons()
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> {
                        popupMenuListener?.onEditItem(item)
                        true
                    }
                    R.id.action_share -> {
                        popupMenuListener?.onShareItem(item)
                        true
                    }
                    R.id.action_delete -> {
                        popupMenuListener?.onDeleteItem(holder.adapterPosition, item)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    interface PopupMenuListener {
        fun onEditItem(dataToEdit: DataWithDataType)
        fun onShareItem(dataToShare: DataWithDataType)
        fun onDeleteItem(pos: Int, dataToDelete: DataWithDataType)
    }
}