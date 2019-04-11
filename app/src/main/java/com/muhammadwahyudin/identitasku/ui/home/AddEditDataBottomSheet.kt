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
            args.putInt("type", type)
            args.putParcelable("_data", dataWithType)
            val fragment = AddEditDataBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    private var _data: DataWithDataType? = null
    private var _selectedDataTypeName: String = ""
    private lateinit var aty: HomeActivity
    var selectedDataTypeId: Int = -1
    var type: Int = 0

    private lateinit var _parent_view: CoordinatorLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            type = it.getInt("type", 0)
            _data = it.getParcelable("_data")
        }
        aty = activity as HomeActivity
        _parent_view = act.find(R.id.parent_home_activity)
        Timber.d("type: $type\n_data: $_data")
        return inflater.inflate(R.layout.bottom_sheet_add_edit_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (type) {
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
        dataTypeStr.add(_data!!.typeName)
        val dataTypeAdapter = ArrayAdapter<String>(aty, android.R.layout.simple_spinner_item, dataTypeStr)
        dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_data_type.setTitle(getString(R.string.spinner_category_title))
        spinner_data_type.adapter = dataTypeAdapter
        spinner_data_type.onSearchableItemClicked(_data!!.typeName, 0)
        spinner_data_type.isEnabled = false
        updateUIBySelectedType(_data!!.typeId)

        // Change Bottomsheet state to Expand
        (this.dialog as BottomSheetDialog?)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
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
        spinner_data_type.setTitle(resources.getString(R.string.spinner_category_title))
        spinner_data_type.setPositiveButton(getString(R.string.btn_close))
        spinner_data_type.adapter = dataTypeAdapter
        spinner_data_type.onItemSelectedListener {
            onItemSelected { adapterView, view, i, l ->
                dataType.find { it.name == dataTypeAdapter.getItem(i) }?.let { selectedDataTypeId = it.id }
                updateUIBySelectedType(selectedDataTypeId)
            }
        }
    }

    private fun updateUIBySelectedType(type: Int) {
        if (spinner_data_type.selectedItem != null) _selectedDataTypeName = spinner_data_type.selectedItem as String
        childFragmentManager.popBackStack()
        when (type) {
            Constants.TYPE_ALAMAT -> {
                changeFragment(AddressInputFragment(), getString(R.string.data_type_name_address), "alamat")
            }
            Constants.TYPE_KTP -> {
                changeFragment(Generic1InputFragment(), getString(R.string.data_type_name_ktp), "ktp")
            }
            Constants.TYPE_EMAIL -> {
                changeFragment(EmailInputFragment(), getString(R.string.data_type_name_email), "email")
            }
            Constants.TYPE_HANDPHONE -> {
                changeFragment(PhoneNumInputFragment(), getString(R.string.data_type_name_phonenum), "phonenum")
            }
            Constants.TYPE_REK_BANK -> {
                changeFragment(BankAccountInputFragment(), getString(R.string.data_type_name_bank_acc), "bank_account")
            }
            Constants.TYPE_CC -> {
                changeFragment(CreditCardInputFragment(), getString(R.string.data_type_name_credit_card), "credit_card")
            }
            Constants.TYPE_BPJS -> {
                changeFragment(Generic1InputFragment(), getString(R.string.data_type_name_bpjs), "bpjs")
            }
            Constants.TYPE_KK -> {
                changeFragment(Generic1InputFragment(), getString(R.string.data_type_name_kk), "kk")
            }
            Constants.TYPE_NPWP -> {
                changeFragment(Generic1InputFragment(), getString(R.string.data_type_name_npwp), "npwp")
            }
            Constants.TYPE_PDAM -> {
                changeFragment(Generic2InputFragment(), getString(R.string.data_type_name_pdam), "pdam")
            }
            Constants.TYPE_PLN -> {
                changeFragment(PlnInputFragment(), getString(R.string.data_type_name_pln), "pln")
            }
            Constants.TYPE_STNK -> {
                changeFragment(Generic2InputFragment(), getString(R.string.data_type_name_stnk), "stnk")
            }
        }
    }

    private fun changeFragment(inputFragment: BaseDataInputFragment<HomeActivity>, typeName: String, tag: String) {
        inputFragment.arguments = Bundle().apply {
            putString(BaseDataInputFragment.TYPE_NAME_KEY, typeName)
            putParcelable(BaseDataInputFragment.DATA_PARCEL_KEY, _data)
        }
        childFragmentManager.beginTransaction()
            .add(R.id.fl_dynamic_data_fields, inputFragment)
            .addToBackStack(tag)
            .commit()
    }


}