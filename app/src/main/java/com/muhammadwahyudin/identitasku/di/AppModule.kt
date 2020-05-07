package com.muhammadwahyudin.identitasku.di

import com.muhammadwahyudin.identitasku.ui.datainput.DataInputViewModelImpl
import com.muhammadwahyudin.identitasku.ui.home.HomeViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModelImpl(get()) }
    viewModel { DataInputViewModelImpl(get()) }
}