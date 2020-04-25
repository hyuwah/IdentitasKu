package com.muhammadwahyudin.identitasku.ui.home

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui._helper.Event
import com.muhammadwahyudin.identitasku.ui._helper.TutorialHelper
import com.muhammadwahyudin.identitasku.ui.home.contract.HomeUiState
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel
import com.muhammadwahyudin.identitasku.ui.settings.SettingsActivity
import com.muhammadwahyudin.identitasku.utils.setGone
import com.muhammadwahyudin.identitasku.utils.setVisible
import com.muhammadwahyudin.identitasku.utils.toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.empty_home_data_list_filtered.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class HomeActivity : BaseActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: HomeViewModelFactory by instance()
    val viewModel: HomeViewModelImpl by viewModels { viewModelFactory }

    private lateinit var dataAdapter: HomeDataAdapter

    private val bsFragment = AddEditDataBottomSheet.newInstance(AddEditDataBottomSheet.ADD)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        parent_home_activity.layoutTransition = LayoutTransition().apply {
            enableTransitionType(LayoutTransition.CHANGING)
        }
        dataAdapter = HomeDataAdapter(mutableListOf()).apply {
            setDiffCallback(HomeDataAdapter.diffCallback())
        }

        initializeUI()
        initializeRecyclerView()
        initTutorial()
        setupBottomBarMenu()
    }

    private fun setupBottomBarMenu() {
        main_bottom_bar.replaceMenu(R.menu.home_bottombar_menu)
        main_bottom_bar.setOnMenuItemClickListener {
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
            true
        }
    }

    private fun initTutorial() {
        TutorialHelper.initAddEditFab(this, fab_add_data)
    }

    private fun initializeRecyclerView() {
        rv_data.layoutManager = LinearLayoutManager(this)
        rv_data.adapter = dataAdapter
        rv_data.itemAnimator = DefaultItemAnimator()
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

        fab_add_data.setOnClickListener {
            if (!bsFragment.isAdded)
                bsFragment.show(supportFragmentManager, bsFragment.tag)
        }

        if (BuildConfig.DEBUG) {
            fab_add_data.setOnLongClickListener {
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
        hideEmptyView()
        showTutorial()
        scrollToItemPos(scrollToPosition)
    }

    private fun showMessage(message: Event<String>) {
        message.getContentIfNotHandled()?.let {
            Snackbar.make(parent_home_activity, it, Snackbar.LENGTH_SHORT)
                .setAnchorView(fab_add_data)
                .show()
        }
    }

    private fun scrollToItemPos(pos: Int = 0) {
        Handler().postDelayed({
            rv_data.smoothScrollToPosition(pos)
        }, 300)
    }

    private fun showTutorial() {
        Handler().postDelayed(
            {
                if (dataAdapter.itemCount > 0)
                    TutorialHelper.initFirstDataItem(this, rv_data)
            },
            1000
        )
    }

    private fun showEmptyView(filters: List<Constants.TYPE> = listOf()) {
        if (filters.isEmpty()) {
            layout_empty_home_list.setVisible()
            layout_empty_home_list_filtered.setGone()
        } else {
            layout_empty_home_list_filtered.tv_filters.text = filters.joinToString(", ") {
                getString(it.stringRes)
            }
            layout_empty_home_list.setGone()
            layout_empty_home_list_filtered.setVisible()
        }
        rv_data.setGone()
    }

    private fun hideEmptyView() {
        layout_empty_home_list.setGone()
        layout_empty_home_list_filtered.setGone()
        rv_data.setVisible()
    }
}
