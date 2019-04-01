package com.muhammadwahyudin.identitasku.ui.home.datainput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.HomeActivity
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment

class BankAccountInputFragment : BaseDataInputFragment<HomeActivity>() {
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.item_home_data_list_rek_bank, container, false)
    }

    override fun setupEditType() {

    }

    override fun setupAddType() {

    }

    override fun setupUIwithData(data: DataWithDataType) {

    }

    override fun setupInputUI() {

    }
}