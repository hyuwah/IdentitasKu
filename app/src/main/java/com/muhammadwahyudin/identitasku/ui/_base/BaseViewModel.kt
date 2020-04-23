package com.muhammadwahyudin.identitasku.ui._base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.SupervisorJob

open class BaseViewModel : ViewModel() {

    protected val spv = SupervisorJob()

    override fun onCleared() {
        super.onCleared()
        spv.cancel()
    }
}