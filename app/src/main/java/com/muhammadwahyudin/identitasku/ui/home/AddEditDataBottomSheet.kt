package com.muhammadwahyudin.identitasku.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui._views.RoundedBottomSheetDialogFragment
import com.muhammadwahyudin.identitasku.ui.home.datainput.*
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import kotlinx.android.synthetic.main.bottom_sheet_add_edit_data.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener
import org.jetbrains.anko.support.v4.act
import timber.log.Timber

class AddEditDataBottomSheet : RoundedBottomSheetDialogFragment() {

    companion object {
        const val ADD = 0
        const val EDIT = 1

        fun newInstance(type: Int, dataWithType: DataWithDataType? = null): AddEditDataBottomSheet {
            val args = Bundle()
            args.putInt("TYPE", type)
            args.putParcelable("DATA", dataWithType)
            val fragment = AddEditDataBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    private var DATA: DataWithDataType? = null
    var TYPE: Int = 0
    var selectedDataTypeId: Int = -1
    var selectedDataTypeName: String = ""
    lateinit var aty: HomeActivity

    private lateinit var parent_view: CoordinatorLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            TYPE = it.getInt("TYPE", 0)
            DATA = it.getParcelable("DATA")
        }
        aty = activity as HomeActivity
        parent_view = act.find(R.id.parent_home_activity)
        Timber.d("TYPE: $TYPE\nDATA: $DATA")
        return inflater.inflate(R.layout.bottom_sheet_add_edit_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (TYPE) {
            ADD -> {
                tv_title.text = getString(R.string.add_new_data_title)
                setupAddDataTypeSpinner()
            }
            EDIT -> {
                tv_title.text = getString(R.string.edit_data_title)
                setupEditDataTypeSpinner()
            }
        }
    }

    private fun setupEditDataTypeSpinner() {
        val dataTypeStr = arrayListOf<String>()
        dataTypeStr.add(DATA!!.typeName)
        val dataTypeAdapter = ArrayAdapter<String>(aty, android.R.layout.simple_spinner_item, dataTypeStr)
        dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_data_type.setTitle("Category")
        spinner_data_type.adapter = dataTypeAdapter
        spinner_data_type.onSearchableItemClicked(DATA!!.typeName, 0)
        spinner_data_type.isEnabled = false
        updateUIBySelectedType(DATA!!.typeId)

        // Change Bottomsheet state to Expand
        (this.dialog as BottomSheetDialog).behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupAddDataTypeSpinner() {
        val dataTypeStr = arrayListOf<String>()
        val dataType = ArrayList<DataType>()
        // Fetch All data type to spinner
        aty.viewModel.getAllDataType().observe(this, Observer {
            it.toCollection(dataType)
            dataTypeStr.clear()
            Timber.d("All DataType $it")
            it.forEach { dataTypeItem ->
                dataTypeStr.add(dataTypeItem.name)
            }
            Timber.d("DataType Raw $dataTypeStr")
            // Filter unique data type that exists on data table
            aty.viewModel.getAllExistingUniqueType().observe(this, Observer { existingUniqueType ->
                Timber.d("existingUniqueType $existingUniqueType")
                existingUniqueType.forEach { item ->
                    dataTypeStr.remove(item.name)
                }
                Timber.d("DataType Filtered $dataTypeStr")
            })
        })

        val dataTypeAdapter = ArrayAdapter<String>(aty, android.R.layout.simple_spinner_item, dataTypeStr)
        dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_data_type.setTitle("Category")
        spinner_data_type.setPositiveButton("Close")
        spinner_data_type.adapter = dataTypeAdapter
        spinner_data_type.onItemSelectedListener {
            onItemSelected { adapterView, view, i, l ->
                dataType.find { it.name == dataTypeAdapter.getItem(i) }?.let { selectedDataTypeId = it.id }
                updateUIBySelectedType(selectedDataTypeId)
            }
        }
    }

    private fun updateUIBySelectedType(type: Int) {
        if (spinner_data_type.selectedItem != null) selectedDataTypeName = spinner_data_type.selectedItem as String
        childFragmentManager.popBackStack()
        when (type) {
            Constants.TYPE_ALAMAT -> {
                changeFragment(AddressInputFragment(), "Alamat", "alamat")
            }
            Constants.TYPE_KTP -> {
                changeFragment(Generic1InputFragment(), "Nomor KTP", "ktp")
            }
            Constants.TYPE_EMAIL -> {
                changeFragment(EmailInputFragment(), "Email", "email")
            }
            Constants.TYPE_HANDPHONE -> {
                changeFragment(PhoneNumInputFragment(), "Nomor Handphone", "phonenum")
            }
            Constants.TYPE_REK_BANK -> {
                changeFragment(BankAccountInputFragment(), "Nomor Rekening Bank", "bank_account")
            }
            Constants.TYPE_CC -> {
                (this.dialog as BottomSheetDialog).behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                changeFragment(CreditCardInputFragment(), "Credit Card", "credit_card")
            }
            Constants.TYPE_BPJS -> {
                changeFragment(Generic1InputFragment(), "Nomor BPJS", "bpjs")
            }
            Constants.TYPE_KK -> {
                changeFragment(Generic1InputFragment(), "Nomor Kartu Keluarga", "kk")
            }
            Constants.TYPE_NPWP -> {
                changeFragment(Generic1InputFragment(), "NPWP", "npwp")
            }
            Constants.TYPE_PDAM -> {
                changeFragment(Generic2InputFragment(), "Nomor PDAM", "pdam")
            }
            Constants.TYPE_PLN -> {
                changeFragment(PlnInputFragment(), "Nomor PLN", "pln")
            }
            Constants.TYPE_STNK -> {
                changeFragment(Generic2InputFragment(), "Nomor STNK", "stnk")
            }
        }
    }

    private fun changeFragment(inputFragment: BaseDataInputFragment<HomeActivity>, typeName: String, tag: String) {
        inputFragment.arguments = Bundle().apply {
            putString(BaseDataInputFragment.TYPE_NAME_KEY, typeName)
            putParcelable(BaseDataInputFragment.DATA_PARCEL_KEY, DATA)
        }
        childFragmentManager.beginTransaction()
            .add(R.id.fl_dynamic_data_fields, inputFragment)
            .addToBackStack(tag)
            .commit()
    }


}