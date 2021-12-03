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

    var _errorBaseLiveData = MutableLiveData<Event<String>>()

    open fun handleError(error: Throwable){
        _errorBaseLiveData.postValue(Event(error.message ?: ""))
    }

    fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    abstract fun backPressed(): Boolean

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

}