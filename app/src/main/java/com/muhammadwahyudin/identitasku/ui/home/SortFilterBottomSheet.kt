package com.muhammadwahyudin.identitasku.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants.TYPE
import com.muhammadwahyudin.identitasku.ui._base.BaseBottomSheetFragment
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel.Companion.DEFAULT_SORT
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel.Companion.SORT
import kotlinx.android.synthetic.main.bottom_sheet_sort_filter.*

class SortFilterBottomSheet : BaseBottomSheetFragment() {

    companion object {
        fun newInstance(): SortFilterBottomSheet {
            return SortFilterBottomSheet()
        }
    }

    private val parentViewModel: SortFilterViewModel by activityViewModels<HomeViewModelImpl>()

    init {
        isCancellable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_sort_filter, container, false)
    }

    private var selectedSort = DEFAULT_SORT
    private var selectedFilters = mutableListOf<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedSort = parentViewModel.currentSort
        selectedFilters.apply {
            clear()
            addAll(parentViewModel.currentFilter)
        }

        setupSortView()
        setupFilterView(view)

        btn_close.setOnClickListener { dismiss() }
        btn_apply.setOnClickListener {
            applyFilter()
        }
    }

    private fun setupSortView() {
        when (selectedSort) {
            SORT.NEWEST -> rg_main_sort.check(R.id.rb_main_sort_newest)
            SORT.OLDEST -> rg_main_sort.check(R.id.rb_main_sort_oldest)
            SORT.CATEGORY -> rg_main_sort.check(R.id.rb_main_sort_category)
        }
        rg_main_sort.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_main_sort_newest -> selectedSort = SORT.NEWEST
                R.id.rb_main_sort_oldest -> selectedSort = SORT.OLDEST
                R.id.rb_main_sort_category -> selectedSort = SORT.CATEGORY
            }
        }
    }

    private fun setupFilterView(view: View) {
        selectedFilters.forEach { typeId ->
            val type = TYPE.values().first { it.value == typeId }
            view.findViewById<CheckBox>(type.viewId).isChecked = true
        }
        container_inner_content.children.forEach { childView ->
            if (childView is CheckBox) {
                childView.setOnClickListener { _ ->
                    val type = TYPE.values().first { it.viewId == childView.id }
                    setFilter(childView.isChecked, type)
                }
            }
        }
    }

    private fun setFilter(isChecked: Boolean, type: TYPE) {
        if (isChecked)
            selectedFilters.add(type.value)
        else
            selectedFilters.remove(type.value)
    }

    private fun applyFilter() {
        parentViewModel.sortAndFilter(selectedSort, selectedFilters)
        dismiss()
    }
}