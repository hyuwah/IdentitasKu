package com.muhammadwahyudin.identitasku.ui.home

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui._helper.TutorialHelper
import com.muhammadwahyudin.identitasku.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class HomeActivity : BaseActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: HomeViewModelFactory by instance()
    val viewModel: HomeViewModel by viewModels { viewModelFactory }

    lateinit var dataAdapter: HomeDataAdapter

    private val bsFragment = AddEditDataBottomSheet.newInstance(AddEditDataBottomSheet.ADD)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        parent_home_activity.layoutTransition = LayoutTransition().apply {
            this.enableTransitionType(LayoutTransition.CHANGING)
        }
        dataAdapter = HomeDataAdapter(mutableListOf()).apply {
            setDiffCallback(HomeDataAdapter.diffCallback())
        }

        initializeUI()
        initializeRecyclerView()
        initTutorial()
    }

    private fun initTutorial() {
        TutorialHelper.initAddEditFab(this, fab_add_data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_appbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.home_app_menu_preferences -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeRecyclerView() {
        rv_data.layoutManager = LinearLayoutManager(this)
        rv_data.adapter = dataAdapter
        rv_data.itemAnimator = DefaultItemAnimator()

        rv_data.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab_add_data.visibility == View.VISIBLE) {
                    fab_add_data.hide()
                    actionBar?.hide()
                } else if (dy <= 0 && fab_add_data.visibility != View.VISIBLE) {
                    fab_add_data.show()
                    actionBar?.show()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (fab_add_data.visibility != View.VISIBLE) {
                    fab_add_data.show()
                    actionBar?.show()
                }
            }
        })
    }

    private fun initializeUI() {
        viewModel.getAllDataWithType().observe(this, Observer { ListOfDataWithType ->

            val listToDisplay: List<DataWithDataType> =
                if (getPreferences(Context.MODE_PRIVATE).getBoolean(
                        "setting_pref_list_sort_by_category",
                        false
                    )
                )
                    ListOfDataWithType.sortedBy { it.typeId }.toList()
                // Should convert to List, Default to Immutable AbstractList
                else
                    ListOfDataWithType

            dataAdapter.setDiffNewData(listToDisplay.toMutableList())

            if (ListOfDataWithType.isNullOrEmpty()) showEmptyView()
            else {
                hideEmptyView()
                Handler().postDelayed({
                    if (dataAdapter.itemCount > 0) TutorialHelper.initFirstDataItem(this, rv_data)
                }, 1000)
            }
        })

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

    private fun showEmptyView() {
        layout_empty_home_list.visibility = View.VISIBLE
        rv_data.visibility = View.INVISIBLE
        fab_add_data.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            this.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        }
    }

    private fun hideEmptyView() {
        layout_empty_home_list.visibility = View.INVISIBLE
        rv_data.visibility = View.VISIBLE
        fab_add_data.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            this.gravity = Gravity.BOTTOM or Gravity.END
        }
    }
}
