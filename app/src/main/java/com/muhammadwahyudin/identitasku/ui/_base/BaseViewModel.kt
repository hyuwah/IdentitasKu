package com.muhammadwahyudin.identitasku.ui._base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.identitasku.ui._helper.Event
import kotlinx.coroutines.SupervisorJob

open class BaseViewModel : ViewModel() {

    protected val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessage = _snackbarMessage as LiveData<Event<String>>

    protected val spv = SupervisorJob()

    protected fun postSnackbar(message: String) {
        _snackbarMessage.postValue(Event(message))
    }

    protected fun setSnackbar(message: String) {
        _snackbarMessage.value = Event(message)
    }

    override fun onCleared() {
        super.onCleared()
        spv.cancel()
    }
}