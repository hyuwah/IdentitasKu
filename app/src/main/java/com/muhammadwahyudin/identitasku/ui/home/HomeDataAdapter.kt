package com.muhammadwahyudin.identitasku.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants.TYPE
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.HomeDataAdapter.Companion.LAYOUT.CREDIT_CARD
import com.muhammadwahyudin.identitasku.ui.home.HomeDataAdapter.Companion.LAYOUT.DEFAULT
import com.muhammadwahyudin.identitasku.ui.home.HomeDataAdapter.Companion.LAYOUT.GENERIC_1
import com.muhammadwahyudin.identitasku.ui.home.HomeDataAdapter.Companion.LAYOUT.GENERIC_2
import com.muhammadwahyudin.identitasku.ui.home.HomeDataAdapter.Companion.LAYOUT.GENERIC_3
import com.muhammadwahyudin.identitasku.utils.Commons
import com.muhammadwahyudin.identitasku.utils.Commons.shortVibrate
import com.muhammadwahyudin.identitasku.utils.showIcons

class HomeDataAdapter(data: MutableList<DataWithDataType>) :
    BaseMultiItemQuickAdapter<DataWithDataType, BaseViewHolder>(data) {

    companion object {
        object LAYOUT {
            const val GENERIC_1 = 0
            const val GENERIC_2 = 1
            const val GENERIC_3 = 2
            const val CREDIT_CARD = 3
            const val DEFAULT = -1
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

    private var deleteHandler = Handler() // should make this global
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
            TYPE.KTP.value, TYPE.NPWP.value, TYPE.KK.value, TYPE.BPJS.value -> GENERIC_1
            TYPE.ALAMAT.value, TYPE.PDAM.value, TYPE.STNK.value, TYPE.EMAIL.value -> GENERIC_2
            TYPE.HANDPHONE.value, TYPE.PLN.value, TYPE.REK_BANK.value -> GENERIC_3
            TYPE.CC.value -> CREDIT_CARD
            else -> DEFAULT
        }
    }

    override fun convert(holder: BaseViewHolder, item: DataWithDataType) {
        aty = context as HomeActivity
        // Commons (BEWARE! DEFAULT ITEM TYPE ALSO AFFECTED)
        // item.typeName doesn't respect Locale languange!
        holder.setText(R.id.tv_data_type, item.typeName)
        holder.setText(R.id.tv_data_value, item.value)
        holder.setImageResource(R.id.iv_icon, item.type().iconRes)
        holder.getView<ImageButton>(R.id.btn_copy_value).setOnClickListener {
            Commons.copyToClipboard(context, item.value, item.typeName)
        }
        val popupMenu = setupPopupMenu(holder, item)
        holder.getView<ImageButton>(R.id.btn_more).setOnClickListener {
            popupMenu.show()
        }
        setupDataView(holder, item)
    }

    private fun setupPopupMenu(holder: BaseViewHolder, item: DataWithDataType): PopupMenu {
        return PopupMenu(context, holder.getView(R.id.btn_more)).apply {
            inflate(R.menu.home_item_action_menu)
            showIcons()
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> {
                        val bs = AddEditDataBottomSheet
                            .newInstance(AddEditDataBottomSheet.EDIT, item)
                        bs.show(aty.supportFragmentManager, bs.tag)
                        true
                    }
                    R.id.action_share -> {
                        onItemShare(holder.adapterPosition)
                        true
                    }
                    R.id.action_delete -> {
                        onItemDismiss(holder.adapterPosition)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupDataView(holder: BaseViewHolder, item: DataWithDataType) {
        when (holder.itemViewType) {
            GENERIC_1 -> {
                // No additional setup
            }
            GENERIC_2 -> {
                if (!item.attr1.isNullOrEmpty()) {
                    holder.getView<TextView>(R.id.tv_data_keterangan).visibility = View.VISIBLE
                    holder.setText(R.id.tv_data_keterangan, item.attr1)
                } else {
                    holder.getView<TextView>(R.id.tv_data_keterangan).visibility = View.GONE
                }
            }
            GENERIC_3 -> {
                if (!item.attr1.isNullOrEmpty()) {
                    holder.getView<TextView>(R.id.tv_data_keterangan).visibility = View.VISIBLE
                    holder.setText(R.id.tv_data_keterangan, item.attr1)
                } else {
                    holder.getView<TextView>(R.id.tv_data_keterangan).visibility = View.GONE
                }
                if (!item.attr2.isNullOrEmpty()) {
                    holder.getView<TextView>(R.id.tv_data_attr2).visibility = View.VISIBLE
                    holder.setText(R.id.tv_data_attr2, item.attr2)
                    holder.setTextColor(R.id.tv_data_attr2, Color.BLACK)
                } else {
                    holder.getView<TextView>(R.id.tv_data_attr2).visibility = View.INVISIBLE
                }
            }
            CREDIT_CARD -> {
                if (!item.attr1.isNullOrEmpty()) {
                    holder.getView<TextView>(R.id.tv_data_keterangan).visibility = View.VISIBLE
                    holder.setText(R.id.tv_data_keterangan, item.attr1)
                } else {
                    holder.getView<TextView>(R.id.tv_data_keterangan).visibility = View.GONE
                }
            }
            DEFAULT -> {
                // NO TYPE WILL USE THIS
            }
        }
    }

    private fun onItemShare(position: Int) {

        notifyItemChanged(position)
        // Need to takeout from adapter and implement on activity
        // ShareItem(category,message)
        val dataToShare = data[position]
        // Share Intent
        // TODO differentiate based on data category
        var message = "${dataToShare.typeName}: ${dataToShare.value}"
        when (dataToShare.type()) {
            TYPE.REK_BANK -> message =
                "${dataToShare.typeName}: (${dataToShare.attr2}) ${dataToShare.value}"
            TYPE.HANDPHONE,
            TYPE.ALAMAT -> {
                if (!dataToShare.attr1.isNullOrEmpty()) message =
                    "${dataToShare.typeName} (${dataToShare.attr1}) : ${dataToShare.value}"
            }
        }
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        aty.startActivity(
            Intent.createChooser(
                sendIntent, String.format(
                    aty.getString(R.string.share_intent_chooser_title),
                    dataToShare.typeName
                )
            )
        )
    }

    private fun onItemDismiss(position: Int) {
        // Vibrate
        shortVibrate(context)

        val dataToDelete = data[position]
        data.removeAt(position)
        notifyItemRemoved(position)

        // clear handler callback first to avoid bug when swiping >1 data in short duration
        deleteHandler.removeCallbacksAndMessages(null)

        // should add to list of datasToDelete
        datasToDelete.add(dataToDelete)

        // Handler to run data deletion on db after snackbar disappear
        deleteHandler.postDelayed(
            { aty.viewModel.deleteDatas(datasToDelete) },
            3500
        ) // delete list of data

        // Show snackbar with undo button
        Snackbar
            .make(
                aty.findViewById(R.id.parent_home_activity),
                dataToDelete.typeName + aty.getString(R.string.snackbar_data_deleted),
                Snackbar.LENGTH_LONG
            )
            .setAction(aty.getString(R.string.snackbar_btn_undo)) {
                data.add(position, dataToDelete)
                notifyItemInserted(position)
                // check if datasToDelete > 1
                if (datasToDelete.size > 1) {
                    // don't cancel the handler,
                    // just remove canceled / last data from list of datasToDelete
                    datasToDelete.remove(dataToDelete)
                } else {
                    // cancel Handler
                    deleteHandler.removeCallbacksAndMessages(null)
                }
            }
            .setAnchorView(aty.findViewById<FloatingActionButton>(R.id.fab_add_data))
            .show()
    }
}