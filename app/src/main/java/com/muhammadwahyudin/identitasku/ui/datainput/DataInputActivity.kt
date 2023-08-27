package com.muhammadwahyudin.identitasku.ui.datainput

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.Data
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.databinding.ActivityDataInputBinding
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui.datainput.contract.DataInputViewModel
import com.muhammadwahyudin.identitasku.ui.datainput.fragment.CreditCardInputFragment
import com.muhammadwahyudin.identitasku.ui.datainput.fragment.DataInputLogic
import com.muhammadwahyudin.identitasku.ui.datainput.fragment.GenericInputFragment
import com.muhammadwahyudin.identitasku.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Need optimization
 */
class DataInputActivity : BaseActivity() {

    companion object {
        const val ADD = 0
        const val EDIT = 1

        const val RC_NEW_DATA = 100
        const val RC_REFRESH_DATA = 200

        private object KEY {
            const val MODE = "mode"
            const val DATA = "data"
        }

        fun launch(activity: Activity, mode: Int, data: DataWithDataType? = null) {
            val requestCode = if (mode == ADD) RC_NEW_DATA else RC_REFRESH_DATA
            activity.launchActivity<DataInputActivity>(requestCode) {
                putExtra(KEY.MODE, mode)
                putExtra(KEY.DATA, data)
            }
        }
    }

    private val bind by viewBinding(ActivityDataInputBinding::inflate)
    private val viewModel: DataInputViewModel by viewModel<DataInputViewModelImpl>()
    private lateinit var bsBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bsBehavior = BottomSheetBehavior.from(bind.bottomSheet)
        bsBehavior.isHideable = false
        bsBehavior.isDraggable = false
        bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bind.btnSave.setOnClickListener {
            hideKeyboard()
            viewModel.saveData()
        }

        intent.extras?.let { bundle ->
            val mode = bundle.getInt(KEY.MODE)
            viewModel.setMode(mode)
            when (mode) {
                ADD -> setupModeAddUi()
                EDIT -> setupModeEditUi(bundle)
            }
        }
        observeLiveData()
    }

    override fun onBackPressed() {
        if (viewModel.hasUnsavedData())
            showUnsavedChangesDialog()
        else
            super.onBackPressed()
    }

    private fun showUnsavedChangesDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_unsaved_changes))
            .setMessage(getString(R.string.dialog_message_unsaved_changes))
            .setPositiveButton(getString(R.string.dialog_positive_unsaved_changes)) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_negative_unsaved_changes)) { dialog, _ ->
                dialog.dismiss()
                super.onBackPressed()
            }
            .create()
        alertDialog.show()
    }

    private fun observeLiveData() {
        viewModel.isOperationSuccess.observe(this, { (isSuccess, mode) ->
            if (isSuccess) {
                val rc = if (mode == ADD) RC_NEW_DATA else RC_REFRESH_DATA
                setResult(rc)
                finish()
            }
        })
        viewModel.isSaveButtonEnabled.observe(this, {
            bind.btnSave.isEnabled = it
        })
    }

    private fun setupModeAddUi() {
        title = "Tambahkan Data Baru"

        bsBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bind.bsDimBg.setVisible()
                bind.bsDimBg.alpha = slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (bsBehavior.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        bind.bsDimBg.setGone()
                        bind.dropdownCategoryContainer.run {
                            endIconDrawable = getDrawable2(R.drawable.ic_arrow_drop_down_24dp)
                            endIconMode = TextInputLayout.END_ICON_CUSTOM
                        }
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        bind.dropdownCategoryContainer.run {
                            endIconDrawable = getDrawable2(R.drawable.ic_arrow_drop_up_24dp)
                            endIconMode = TextInputLayout.END_ICON_CUSTOM
                        }
                    }
                }
            }
        })
        bind.bsDimBg.setOnClickListener {
            bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
//        bind.dropdownCategory.setOnFocusChangeListener { _, b ->
//            if (b && bsBehavior.state == BottomSheetBehavior.STATE_COLLAPSED)
//                bsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }
        bind.dropdownCategory.setOnClickListener {
            bsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bind.rvCategory.itemAnimator = DefaultItemAnimator()
        bind.rvCategory.adapter = CategoryListAdapter(::onCategoryItemClick).apply {
            addData(Constants.TYPE.values().filter { it != Constants.TYPE.DEFAULT }.toList())
            addHeaderView(layoutInflater.inflate(R.layout.item_category_list_header, null), 0)
        }

        Handler().postDelayed({ bind.dropdownCategory.performClick() }, 500)
    }

    private fun setupModeEditUi(bundle: Bundle) {
        title = "Ubah Data"
        val data = bundle.getParcelable(KEY.DATA) as? DataWithDataType
        data?.let {
            val currentData = Data(
                it.typeId,
                it.value,
                it.attr1,
                it.attr2,
                it.attr3,
                it.attr4,
                it.attr5
            ).apply { id = it.id }
            viewModel.setData(currentData)
            bind.dropdownCategory.setText(it.type().stringRes)
            updateUIBySelectedType(it.type())
        }
    }

    private fun updateUIBySelectedType(type: Constants.TYPE) {
        when (type) {
            Constants.TYPE.ALAMAT -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.Address(getString(R.string.data_type_name_address))
                    )
                )
            }
            Constants.TYPE.KTP -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.KTP(getString(R.string.data_type_name_ktp))
                    )
                )
            }
            Constants.TYPE.EMAIL -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.Email(getString(R.string.data_type_name_email))
                    )
                )
            }
            Constants.TYPE.HANDPHONE -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.Handphone(getString(R.string.data_type_name_phonenum))
                    )
                )
            }
            Constants.TYPE.REK_BANK -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.BankAccount(getString(R.string.data_type_name_bank_acc))
                    )
                )
            }
            Constants.TYPE.CC -> {
                changeFragment(
                    CreditCardInputFragment.newInstance(
                        DataInputLogic.CreditCard(getString(R.string.data_type_name_credit_card))
                    )
                )
            }
            Constants.TYPE.BPJS -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.BPJS(getString(R.string.data_type_name_bpjs))
                    )
                )
            }
            Constants.TYPE.KK -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.KartuKeluarga(getString(R.string.data_type_name_kk))
                    )
                )
            }
            Constants.TYPE.NPWP -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.NPWP(getString(R.string.data_type_name_npwp))
                    )
                )
            }
            Constants.TYPE.PDAM -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.PDAM(getString(R.string.data_type_name_pdam))
                    )
                )
            }
            Constants.TYPE.PLN -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.PLN(getString(R.string.data_type_name_pln))
                    )
                )
            }
            Constants.TYPE.STNK -> {
                changeFragment(
                    GenericInputFragment.newInstance(
                        DataInputLogic.STNK(getString(R.string.data_type_name_stnk))
                    )
                )
            }
            Constants.TYPE.DEFAULT -> {
                // Nothing
            }
        }
    }

    private fun changeFragment(inputFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragment_container, inputFragment)
            .commit()
    }

    private fun onCategoryItemClick(type: Constants.TYPE) {
        bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        if (type == viewModel.currentType) return
        bind.dropdownCategory.setText(getString(type.stringRes))
        viewModel.resetData(type)
        updateUIBySelectedType(type)
    }
}