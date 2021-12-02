package com.lenatopoleva.musicsearch.viewmodel.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lenatopoleva.musicsearch.utils.Event
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {

    protected val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })

    var _errorLiveData = MutableLiveData<Event<String>>()

    abstract fun handleError(error: Throwable)

    fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    abstract fun backPressed(): Boolean

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

}