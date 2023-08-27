package com.muhammadwahyudin.identitasku.di

import com.muhammadwahyudin.identitasku.ui.datainput.DataInputViewModelImpl
import com.muhammadwahyudin.identitasku.ui.home.HomeViewModelImpl
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeViewModelImpl)
    viewModelOf(::DataInputViewModelImpl)
}