package com.muhammadwahyudin.identitasku.ui.datainput

import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants

class CategoryListAdapter(
    private val clickCallback: (item: Constants.TYPE) -> Unit
) :
    BaseQuickAdapter<Constants.TYPE, CategoryListAdapter.ViewHolder>(R.layout.item_category_list) {

    private var lastCheckPos = -1

    override fun convert(holder: ViewHolder, item: Constants.TYPE) {
        holder.rbCategoryOption.apply {
            text = context.getString(item.stringRes)
            this.isChecked = lastCheckPos == holder.adapterPosition
            setOnClickListener {
                lastCheckPos = holder.adapterPosition
                notifyDataSetChanged()
                clickCallback(item)
            }
        }
        holder.ivCategoryOption.apply {
            setImageResource(item.iconRes)
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val rbCategoryOption: RadioButton = itemView.findViewById(R.id.rb_category_option)
        val ivCategoryOption: ImageView = itemView.findViewById(R.id.iv_category_option)
    }
}