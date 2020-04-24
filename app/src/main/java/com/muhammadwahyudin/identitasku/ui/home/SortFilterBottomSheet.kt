package com.muhammadwahyudin.identitasku.ui.home

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.Constants.TYPE
import com.muhammadwahyudin.identitasku.ui._base.BaseBottomSheetFragment
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel.Companion.DEFAULT_SORT
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel.Companion.SORT
import com.muhammadwahyudin.identitasku.databinding.BottomSheetSortFilterBinding as Binding

class SortFilterBottomSheet : BaseBottomSheetFragment<Binding>(Binding::inflate) {

    companion object {
        fun newInstance(): SortFilterBottomSheet {
            return SortFilterBottomSheet()
        }
    }

    private val parentViewModel: SortFilterViewModel by activityViewModels<HomeViewModelImpl>()
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

        binding.btnClose.setOnClickListener { dismiss() }
        binding.btnApply.setOnClickListener { applyFilter() }
        binding.btnReset.setOnClickListener { resetFilter() }
    }

    private fun setupSortView() {
        when (selectedSort) {
            SORT.NEWEST -> binding.rgMainSort.check(R.id.rb_main_sort_newest)
            SORT.OLDEST -> binding.rgMainSort.check(R.id.rb_main_sort_oldest)
            SORT.CATEGORY -> binding.rgMainSort.check(R.id.rb_main_sort_category)
        }
        binding.rgMainSort.setOnCheckedChangeListener { _, id ->
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
        binding.containerInnerContent.children.forEach { childView ->
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

    private fun resetFilter() {
        if (selectedFilters.isNotEmpty()) {
            selectedFilters.clear()
            binding.containerInnerContent.children.forEach {
                if (it is CheckBox) {
                    it.isChecked = false
                }
            }
        }
    }
}