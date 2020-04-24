package com.muhammadwahyudin.identitasku.ui.home

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants.TYPE
import com.muhammadwahyudin.identitasku.data.model.DataType
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui._base.BaseBottomSheetFragment
import com.muhammadwahyudin.identitasku.ui.home.contract.HomeViewModel
import com.muhammadwahyudin.identitasku.ui.home.datainput.*
import com.muhammadwahyudin.identitasku.ui.home.datainput._base.BaseDataInputFragment
import com.muhammadwahyudin.identitasku.databinding.BottomSheetAddEditDataBinding as Binding

class AddEditDataBottomSheet : BaseBottomSheetFragment<Binding>(Binding::inflate) {

    companion object {
        const val ADD = 0
        const val EDIT = 1

        private object KEY {
            const val TYPE = "type"
            const val DATA = "data"
        }

        fun newInstance(type: Int, dataWithType: DataWithDataType? = null): AddEditDataBottomSheet {
            return AddEditDataBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(KEY.TYPE, type)
                    putParcelable(KEY.DATA, dataWithType)
                }
            }
        }
    }

    private var data: DataWithDataType? = null
    private var selectedDataTypeName: String = ""
    private val parentViewModel: HomeViewModel by activityViewModels<HomeViewModelImpl>()
    var selectedDataTypeId: TYPE = TYPE.DEFAULT
    var type: Int = 0

    private lateinit var parent_view: CoordinatorLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            type = it.getInt(KEY.TYPE, 0)
            data = it.getParcelable(KEY.DATA)
        }
        parent_view = requireActivity().findViewById(R.id.parent_home_activity)
        when (type) {
            ADD -> {
                binding.tvTitle.text = getString(R.string.add_new_data_title)
                setupAddDataTypeSpinner()
            }
            EDIT -> {
                binding.tvTitle.text = getString(R.string.edit_data_title)
                setupEditDataTypeSpinner()
            }
        }
    }

    private fun setupEditDataTypeSpinner() {
        val dataTypeStr = arrayListOf<String>()
        dataTypeStr.add(data?.typeName.orEmpty())
        val dataTypeAdapter =
            ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                dataTypeStr
            )
        dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDataType.apply {
            setTitle(getString(R.string.spinner_category_title))
            adapter = dataTypeAdapter
            onSearchableItemClicked(data?.typeName.orEmpty(), 0)
            isEnabled = false
        }
        updateUIBySelectedType(data?.type() ?: TYPE.DEFAULT)
    }

    private fun setupAddDataTypeSpinner() {
        val dataTypeStr = arrayListOf<String>()
        val dataType = ArrayList<DataType>()
        // Fetch All data type to spinner
        parentViewModel.getAllDataType()
            .observe(viewLifecycleOwner, Observer {
                it.toCollection(dataType)
                dataTypeStr.clear()
                it.forEach { dataTypeItem ->
                    dataTypeStr.add(dataTypeItem.name)
                }
                // Filter unique data type that exists on data table
                parentViewModel.getAllExistingUniqueType()
                    .observe(viewLifecycleOwner, Observer { existingUniqueType ->
                        existingUniqueType.forEach { item ->
                            dataTypeStr.remove(item.name)
                        }
                    })
            })

        val dataTypeAdapter =
            ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                dataTypeStr
            )
        dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDataType.apply {
            setTitle(resources.getString(R.string.spinner_category_title))
            setPositiveButton(getString(R.string.btn_close))
            adapter = dataTypeAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    dataType.find { it.name == adapterView.adapter.getItem(i) }
                        ?.let { dataType ->
                            selectedDataTypeId = TYPE.values().first { it.value == dataType.id }
                        }
                    updateUIBySelectedType(selectedDataTypeId)
                }
            }
        }
    }

    private fun updateUIBySelectedType(type: TYPE) {
        if (binding.spinnerDataType.selectedItem != null)
            selectedDataTypeName = binding.spinnerDataType.selectedItem as String
        childFragmentManager.popBackStack()
        when (type) {
            TYPE.ALAMAT -> {
                changeFragment(
                    AddressInputFragment(),
                    getString(R.string.data_type_name_address),
                    "alamat"
                )
            }
            TYPE.KTP -> {
                changeFragment(
                    Generic1InputFragment(),
                    getString(R.string.data_type_name_ktp),
                    "ktp"
                )
            }
            TYPE.EMAIL -> {
                changeFragment(
                    EmailInputFragment(),
                    getString(R.string.data_type_name_email),
                    "email"
                )
            }
            TYPE.HANDPHONE -> {
                changeFragment(
                    PhoneNumInputFragment(),
                    getString(R.string.data_type_name_phonenum),
                    "phonenum"
                )
            }
            TYPE.REK_BANK -> {
                changeFragment(
                    BankAccountInputFragment(),
                    getString(R.string.data_type_name_bank_acc),
                    "bank_account"
                )
            }
            TYPE.CC -> {
                changeFragment(
                    CreditCardInputFragment(),
                    getString(R.string.data_type_name_credit_card),
                    "credit_card"
                )
            }
            TYPE.BPJS -> {
                changeFragment(
                    Generic1InputFragment(),
                    getString(R.string.data_type_name_bpjs),
                    "bpjs"
                )
            }
            TYPE.KK -> {
                changeFragment(
                    Generic1InputFragment(),
                    getString(R.string.data_type_name_kk),
                    "kk"
                )
            }
            TYPE.NPWP -> {
                changeFragment(
                    Generic1InputFragment(),
                    getString(R.string.data_type_name_npwp),
                    "npwp"
                )
            }
            TYPE.PDAM -> {
                changeFragment(
                    Generic2InputFragment(),
                    getString(R.string.data_type_name_pdam),
                    "pdam"
                )
            }
            TYPE.PLN -> {
                changeFragment(
                    PlnInputFragment(),
                    getString(R.string.data_type_name_pln),
                    "pln"
                )
            }
            TYPE.STNK -> {
                changeFragment(
                    Generic2InputFragment(),
                    getString(R.string.data_type_name_stnk),
                    "stnk"
                )
            }
        }
    }

    private fun changeFragment(
        inputFragment: BaseDataInputFragment,
        typeName: String,
        tag: String
    ) {
        inputFragment.arguments = Bundle().apply {
            putString(BaseDataInputFragment.TYPE_NAME_KEY, typeName)
            putParcelable(BaseDataInputFragment.DATA_PARCEL_KEY, data)
        }
        childFragmentManager.beginTransaction()
            .add(R.id.fl_dynamic_data_fields, inputFragment)
            .addToBackStack(tag)
            .commit()
    }
}