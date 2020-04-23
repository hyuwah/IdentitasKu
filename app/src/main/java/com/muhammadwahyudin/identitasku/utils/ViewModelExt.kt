package com.muhammadwahyudin.identitasku.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun ViewModel.ioLaunch(supervisor: Job? = null, block: suspend CoroutineScope.() -> Unit) {
    val context = if (supervisor != null) Dispatchers.IO + supervisor else Dispatchers.IO
    this.viewModelScope.launch(context) {
        block.invoke(this)
    }
}