package com.muhammadwahyudin.identitasku.ui.home

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.databinding.ActivityHomeBinding
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui._helper.Event
import com.muhammadwahyudin.identitasku.ui._helper.TutorialHelper
import com.muhammadwahyudin.identitasku.ui.datainput.DataInputActivity
import com.muhammadwahyudin.identitasku.ui.datainput.DataInputActivity.Companion.RC_NEW_DATA
import com.muhammadwahyudin.identitasku.ui.datainput.DataInputActivity.Companion.RC_REFRESH_DATA
import com.muhammadwahyudin.identitasku.ui.home.contract.HomeUiState
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel
import com.muhammadwahyudin.identitasku.ui.settings.SettingsActivity
import com.muhammadwahyudin.identitasku.utils.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity(), HomeDataAdapter.PopupMenuListener {
    
    private val binding by viewBinding(ActivityHomeBinding::inflate)

    private val viewModel: HomeViewModelImpl by viewModel()

    private var backToExitPressed = false
    private lateinit var dataAdapter: HomeDataAdapter
    private lateinit var menu: Menu

    private var deleteHandler = Handler()
    private var datasToDelete = arrayListOf<DataWithDataType>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.layoutTransition = LayoutTransition().apply {
            enableTransitionType(LayoutTransition.CHANGING)
        }
        dataAdapter = HomeDataAdapter(mutableListOf()).apply {
            setDiffCallback(HomeDataAdapter.diffCallback())
            popupMenuListener = this@HomeActivity
        }

        initializeUI()
        initializeRecyclerView()
        initTutorial()
        setupBottomBarMenu()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_NEW_DATA -> {
                // Reload list & scroll
                viewModel.loadAllData()
            }
            RC_REFRESH_DATA -> {
                // Refresh without scroll
                viewModel.refreshData()
            }
        }
    }

    override fun onEditItem(dataToEdit: DataWithDataType) {
        DataInputActivity.launch(this, DataInputActivity.EDIT, dataToEdit)
    }

    override fun onShareItem(dataToShare: DataWithDataType) {
        // TODO differentiate based on data category
        val attr1 = dataToShare.attr1.orEmpty()
        val attr2 = dataToShare.attr2.orEmpty()
        val message = buildString {
            append("${dataToShare.typeName}:")
            if (attr1.isNotEmpty()) {
                append(" ($attr1)")
            }
            if (attr2.isNotEmpty()) {
                append(" ($attr2)")
            }
            append(" ${dataToShare.value}")
        }
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        startActivity(
            Intent.createChooser(
                sendIntent, String.format(
                    getString(R.string.share_intent_chooser_title),
                    dataToShare.typeName
                )
            )
        )
    }

    override fun onDeleteItem(pos: Int, dataToDelete: DataWithDataType) {
        Commons.shortVibrate(this)

        dataAdapter.data.removeAt(pos)
        dataAdapter.notifyItemRemoved(pos)

        // clear handler callback first to avoid bug when swiping >1 data in short duration
        deleteHandler.removeCallbacksAndMessages(null)

        // should add to list of datasToDelete
        datasToDelete.add(dataToDelete)

        // Handler to run data deletion on db after snackbar disappear
        deleteHandler.postDelayed({ viewModel.deleteDatas(datasToDelete) }, 3500)
        // delete list of data

        // Show snackbar with undo button
        Snackbar
            .make(
                binding.root,
                dataToDelete.typeName + getString(R.string.snackbar_data_deleted),
                Snackbar.LENGTH_LONG
            )
            .setAction(getString(R.string.snackbar_btn_undo)) {
                dataAdapter.data.add(pos, dataToDelete)
                dataAdapter.notifyItemInserted(pos)
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
            .setAnchorView(binding.fabAddData)
            .show()
    }

    private fun setupBottomBarMenu() {
        binding.mainBottomBar.replaceMenu(R.menu.home_bottombar_menu)
        menu = binding.mainBottomBar.menu
        binding.mainBottomBar.setSafeOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_action_sort_filter -> {
                    SortFilterBottomSheet.newInstance().show(supportFragmentManager)
                }
                R.id.menu_action_search -> {
                    toast("To Search Activity")
                }
                R.id.menu_action_setting -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
            }
        }
    }

    private fun initTutorial() {
        TutorialHelper.initAddEditFab(this, binding.fabAddData)
    }

    private fun initializeRecyclerView() {
        binding.layoutHomeContent.rvData.run {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = dataAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun initializeUI() {
        viewModel.uiState.observe(this, Observer {
            when (it) {
                HomeUiState.EmptyNoData -> showEmptyView()
                is HomeUiState.EmptyFiltered -> showEmptyView(it.filter)
                is HomeUiState.HasData -> showData(it.datas, it.sort, it.filter, it.scrollToPos)
            }
        })
        viewModel.snackbarMessage.observe(this, ::showMessage)
        viewModel.loadAllData()

        binding.fabAddData.setOnClickListener {
            DataInputActivity.launch(this, DataInputActivity.ADD)
        }

        if (BuildConfig.DEBUG) {
            binding.fabAddData.setOnLongClickListener {
                viewModel.debugPopulateData()
                true
            }
        }
    }

    private fun showData(
        datas: List<DataWithDataType>,
        sort: SortFilterViewModel.Companion.SORT,
        filters: List<Constants.TYPE>,
        scrollToPosition: Int
    ) {
        dataAdapter.setDiffNewData(datas.toMutableList())
        setFilterIcon(filters)
        setActiveFilterView(filters)
        hideEmptyView()
        showTutorial()
        scrollToItemPos(scrollToPosition)
    }

    private fun showMessage(message: Event<String>) {
        message.getContentIfNotHandled()?.let {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                .setAnchorView(binding.fabAddData)
                .show()
        }
    }

    private fun scrollToItemPos(pos: Int = 0) {
        if (pos != -1)
            Handler().postDelayed({
                binding.layoutHomeContent.rvData.smoothScrollToPosition(pos)
            }, 300)
    }

    private fun showTutorial() {
        Handler().postDelayed(
            {
                if (dataAdapter.itemCount > 0)
                    TutorialHelper.initFirstDataItem(
                        this,
                        binding.layoutHomeContent.rvData,
                        binding.mainBottomBar.rootView.findViewById(R.id.menu_action_sort_filter)
                    )
            },
            1000
        )
    }

    private fun showEmptyView(filters: List<Constants.TYPE> = listOf()) {
        if (filters.isEmpty()) {
            binding.layoutEmptyHomeList.root.setVisible()
            binding.layoutEmptyHomeListFiltered.root.setGone()
        } else {
            binding.layoutEmptyHomeListFiltered.tvFilters.text = filters.joinToString(", ") {
                getString(it.stringRes).replace("Nomor ", "", true)
            }
            binding.layoutEmptyHomeList.root.setGone()
            binding.layoutEmptyHomeListFiltered.root.setVisible()
        }
        setFilterIcon(filters)
        binding.layoutHomeContent.root.setGone()
    }

    private fun hideEmptyView() {
        binding.layoutEmptyHomeList.root.setGone()
        binding.layoutEmptyHomeListFiltered.root.setGone()
        binding.layoutHomeContent.root.setVisible()
    }

    private fun setFilterIcon(filters: List<Constants.TYPE>) {
        val sortFilterMenu = menu.findItem(R.id.menu_action_sort_filter)
        if (filters.isEmpty())
            sortFilterMenu.setIcon(R.drawable.ic_filter_list_white_24dp)
        else
            sortFilterMenu.setIcon(R.drawable.ic_filter_list_active_white)
    }

    private fun setActiveFilterView(filters: List<Constants.TYPE>) {
        if (filters.isEmpty()) {
            binding.layoutHomeContent.containerTopbar.setGone()
        } else {
            binding.layoutHomeContent.containerTopbar.setVisible()
            binding.layoutHomeContent.tvFiltersActive.apply {
                text = filters.joinToString(", ") {
                    getString(it.stringRes).replace("Nomor ", "", true)
                }
                isSelected = true
            }
        }
    }

    // Double back to exit
    override fun onBackPressed() {
        if (backToExitPressed || supportFragmentManager.backStackEntryCount != 0) {
            super.onBackPressed()
            return
        }
        this.backToExitPressed = true
        toast(getString(R.string.exit_double_tap_message))
        Handler().postDelayed({ backToExitPressed = false }, 2000)
    }
}
