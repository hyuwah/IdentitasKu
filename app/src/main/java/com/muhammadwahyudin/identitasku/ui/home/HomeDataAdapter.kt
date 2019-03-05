package com.muhammadwahyudin.identitasku.ui.home

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.utils.Commons


class HomeDataAdapter(data: List<DataWithDataType>) :
    BaseMultiItemQuickAdapter<DataWithDataType, BaseViewHolder>(data) {
    init {
        addItemType(Constants.TYPE_KTP, R.layout.item_home_data_list_ktp)
        addItemType(Constants.TYPE_REK_BANK, R.layout.item_home_data_list_rek_bank)
        addItemType(Constants.TYPE_DEFAULT, R.layout.item_home_data_list)
    }

    override fun convert(helper: BaseViewHolder, item: DataWithDataType) {
        // Edit
//        helper.itemView.setOnClickListener {
//            val bs = AddEditDataBottomSheet.newInstance(AddEditDataBottomSheet.EDIT, item)
//            bs.show((mContext as HomeActivity).supportFragmentManager, bs.tag)
//        }

        // Delete
//        helper.itemView.setOnLongClickListener {
//            mContext.alert(Appcompat, "Yakin mau di delete?", "Wait!") {
//                positiveButton("Ya") {
//                    Timber.d("Delete")
//                    (mContext as HomeActivity).viewModel.deleteData(item)
//                }
//                negativeButton("Nggak") {}
//                show()
//            }
//            true
//        }

        //Commons
        helper.setText(R.id.tv_data_type, item.typeName)
        helper.setText(R.id.tv_data_value, item.value)
        helper.setOnClickListener(R.id.btn_copy_value) {
            Commons.copyToClipboard(mContext, item.typeName)
        }

        when (item.itemType) {
            Constants.TYPE_KTP -> {


            }
            Constants.TYPE_REK_BANK -> {
                helper.setText(R.id.tv_data_bank, item.attr1)
            }
            Constants.TYPE_DEFAULT -> {

            }
        }

    }

    fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
//        val tmp = data.removeAt(fromPosition)
//        data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, tmp)
        notifyItemMoved(fromPosition, toPosition)
    }
}