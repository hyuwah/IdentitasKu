package com.muhammadwahyudin.identitasku.ui.home

import android.graphics.Color
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.snackbar.Snackbar
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_ALAMAT
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_BPJS
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_CC
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_EMAIL
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_HANDPHONE
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_KK
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_KTP
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_NPWP
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_PDAM
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_PLN
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_REK_BANK
import com.muhammadwahyudin.identitasku.data.Constants.TYPE_STNK
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui._helper.SwipeItemTouchHelper
import com.muhammadwahyudin.identitasku.utils.Commons
import org.jetbrains.anko.find

class HomeDataAdapter(data: List<DataWithDataType>) :
    BaseMultiItemQuickAdapter<DataWithDataType, BaseViewHolder>(data), SwipeItemTouchHelper.SwipeHelperAdapter {

    companion object LAYOUT {
        const val GENERIC_1 = 0
        const val GENERIC_2 = 1
        const val GENERIC_3 = 2
        const val CREDIT_CARD = 3
        const val DEFAULT = -1
    }

    private var deleteHandler = Handler() // should make this global
    private var _swipedDatas = ArrayList<DataWithDataType>()
    private var datasToDelete = arrayListOf<DataWithDataType>()
    private lateinit var aty: HomeActivity

    init {
        // Init item type
        addItemType(GENERIC_1, R.layout.item_home_data_list_generic_1)
        addItemType(GENERIC_2, R.layout.item_home_data_list_generic_2)
        addItemType(GENERIC_3, R.layout.item_home_data_list_generic_3)
        addItemType(CREDIT_CARD, R.layout.item_home_data_list_generic_2)
        addItemType(DEFAULT, R.layout.item_home_data_list_generic_2)

        // itemType generic 1 value [ktp, npwp, kk, bpjs]
        // itemType generic 2 value [address, pdam, stnk, email]
        // itemType generic 3 (2 value + 1 spinner data) [hp, pln, rekbank]
        // itemType cc
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].typeId) {
            TYPE_KTP, TYPE_NPWP, TYPE_KK, TYPE_BPJS -> GENERIC_1
            TYPE_ALAMAT, TYPE_PDAM, TYPE_STNK, TYPE_EMAIL -> GENERIC_2
            TYPE_HANDPHONE, TYPE_PLN, TYPE_REK_BANK -> GENERIC_3
            TYPE_CC -> CREDIT_CARD
            else -> DEFAULT
        }
    }

    override fun convert(helper: BaseViewHolder, item: DataWithDataType) {
        aty = mContext as HomeActivity
        // Edit
        helper.itemView.setOnLongClickListener {
            val bs = AddEditDataBottomSheet.newInstance(AddEditDataBottomSheet.EDIT, item)
            bs.show(aty.supportFragmentManager, bs.tag)
            true
        }

        //Commons (BEWARE! DEFAULT ITEM TYPE ALSO AFFECTED)
        helper.setText(R.id.tv_data_type, item.typeName)
        helper.setText(R.id.tv_data_value, item.value)
        helper.setOnClickListener(R.id.btn_copy_value) {
            Commons.copyToClipboard(mContext, item.value, item.typeName)
        }

        // Specific data
        when (helper.itemViewType) {
            GENERIC_1 -> {
                when (item.typeId) {
                    TYPE_KTP -> {
                        helper.setImageResource(R.id.iv_icon, R.drawable.ic_ktp)
                    }
                    TYPE_NPWP -> {
                        helper.setImageResource(R.id.iv_icon, R.drawable.ic_npwp)
                    }
                    TYPE_KK -> {
                        helper.setImageResource(R.id.iv_icon, R.drawable.ic_kk)
                    }
                    TYPE_BPJS -> {
                        helper.setImageResource(R.id.iv_icon, R.drawable.ic_bpjs)
                    }
                }
            }
            GENERIC_2 -> {
                if (!item.attr1.isNullOrEmpty()) {
                    helper.getView<TextView>(R.id.tv_data_keterangan).visibility = View.VISIBLE
                    helper.setText(R.id.tv_data_keterangan, item.attr1)
                } else {
                    helper.getView<TextView>(R.id.tv_data_keterangan).visibility = View.GONE
                }
                when (item.typeId) {
                    TYPE_ALAMAT -> helper.setImageResource(R.id.iv_icon, R.drawable.ic_address)
                    TYPE_PDAM -> helper.setImageResource(R.id.iv_icon, R.drawable.ic_pdam)
                    TYPE_STNK -> helper.setImageResource(R.id.iv_icon, R.drawable.ic_stnk)
                    TYPE_EMAIL -> helper.setImageResource(R.id.iv_icon, R.drawable.ic_email)
                }
            }
            GENERIC_3 -> {
                if (!item.attr1.isNullOrEmpty()) {
                    helper.getView<TextView>(R.id.tv_data_keterangan).visibility = View.VISIBLE
                    helper.setText(R.id.tv_data_keterangan, item.attr1)
                } else {
                    helper.getView<TextView>(R.id.tv_data_keterangan).visibility = View.GONE
                }
                if (!item.attr2.isNullOrEmpty()) {
                    helper.getView<TextView>(R.id.tv_data_attr2).visibility = View.VISIBLE
                    helper.setText(R.id.tv_data_attr2, item.attr2)
                    helper.setTextColor(R.id.tv_data_attr2, Color.BLACK)
                } else {
                    helper.getView<TextView>(R.id.tv_data_attr2).visibility = View.GONE
                }
                when (item.typeId) {
                    TYPE_HANDPHONE -> helper.setImageResource(R.id.iv_icon, R.drawable.ic_handphone)
                    TYPE_PLN -> helper.setImageResource(R.id.iv_icon, R.drawable.ic_pln)
                    TYPE_REK_BANK -> helper.setImageResource(R.id.iv_icon, R.drawable.ic_bank_account)
                }
            }
            CREDIT_CARD -> {
                if (!item.attr1.isNullOrEmpty()) {
                    helper.getView<TextView>(R.id.tv_data_keterangan).visibility = View.VISIBLE
                    helper.setText(R.id.tv_data_keterangan, item.attr1)
                } else {
                    helper.getView<TextView>(R.id.tv_data_keterangan).visibility = View.GONE
                }
                helper.setImageResource(R.id.iv_icon, R.drawable.ic_credit_card)
            }
            DEFAULT -> {
                // NO TYPE WILL USE THIS
            }
        }

    }

    // SWIPE & DRAG

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                for (item in _swipedDatas) {
                    val index_removed = data.indexOf(item)
                    if (index_removed != -1) {
                        data.removeAt(index_removed)
                        notifyItemRemoved(index_removed)
                    }
                }
                _swipedDatas.clear()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onItemDismiss(position: Int) {
        val dataToDelete = data[position]
        data.removeAt(position)
        notifyItemRemoved(position)

        // clear handler callback first to avoid bug when swiping more than one data in short duration
        deleteHandler.removeCallbacksAndMessages(null)

        // should add to list of datasToDelete
        datasToDelete.add(dataToDelete)

        // Handler to run data deletion on db after snackbar disappear
        deleteHandler.postDelayed({ aty.viewModel.deleteDatas(datasToDelete) }, 3500) // delete list of data

        // Show snackbar with undo button
        Snackbar.make(aty.find(R.id.parent_home_activity), "${dataToDelete.typeName} deleted", Snackbar.LENGTH_LONG)
            .setAction(aty.getString(R.string.snackbar_btn_undo)) {
                data.add(position, dataToDelete)
                notifyItemInserted(position)
                // check if datasToDelete > 1
                if (datasToDelete.size > 1) {
                    // don't cancel the handler, just remove canceled / last data from list of datasToDelete
                    datasToDelete.remove(dataToDelete)
                } else {
                    // cancel Handler
                    deleteHandler.removeCallbacksAndMessages(null)
                }
            }.show()
    }
}