package com.muhammadwahyudin.identitasku.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muhammadwahyudin.identitasku.data.repository.IAppRepository

class HomeViewModelFactory(private val appRepository: IAppRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModelImpl(appRepository) as T
    }
}