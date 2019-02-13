package com.muhammadwahyudin.identitasku.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.ui._views.RoundedBottomSheetDialogFragment

class AddDataBottomSheet : RoundedBottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_data, container, false)
    }


}