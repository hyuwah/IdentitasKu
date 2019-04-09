package com.muhammadwahyudin.identitasku.ui.home

import android.animation.LayoutTransition
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.identitasku.BuildConfig
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.ui._base.BaseActivity
import com.muhammadwahyudin.identitasku.ui._helper.SwipeItemTouchHelper
import kotlinx.android.synthetic.main.activity_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class HomeActivity : BaseActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: HomeViewModelFactory by instance()
    lateinit var viewModel: HomeViewModel

    lateinit var dataAdapter: HomeDataAdapter

    val bsFragment = AddEditDataBottomSheet.newInstance(AddEditDataBottomSheet.ADD)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        parent_home_activity.layoutTransition = LayoutTransition().apply {
            this.enableTransitionType(LayoutTransition.CHANGING)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        dataAdapter = HomeDataAdapter(emptyList())
        val callback = SwipeItemTouchHelper(dataAdapter)
        callback.icon = ContextCompat.getDrawable(this, R.drawable.ic_delete_white_24dp)
        callback.bgColorCode = Color.RED
        val mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(rv_data)

        initializeUI()
        initializeRecyclerView()
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
            dataAdapter.setNewData(ListOfDataWithType)
            if (ListOfDataWithType.isNullOrEmpty()) showEmptyView()
            else hideEmptyView()
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
