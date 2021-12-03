package com.lenatopoleva.musicsearch.viewmodel.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.activity.ISplashInteractor
import com.lenatopoleva.musicsearch.utils.Event
import com.lenatopoleva.musicsearch.utils.ui.alertdialog.AlertDialogListener
import kotlinx.coroutines.*

class SplashViewModel(private val interactor: ISplashInteractor,
                      private val dispatcherProvider: IDispatcherProvider
): ViewModel(), AlertDialogListener {

    private val _authUserLiveDate = MutableLiveData<Event<Boolean>>()
    val authUserLiveDate: LiveData<Event<Boolean>> = _authUserLiveDate

    private var _errorLiveData = MutableLiveData<Event<String>>()
    val errorLiveData: LiveData<Event<String>> = _errorLiveData

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })

    private fun handleError(error: Throwable) {
        _errorLiveData.postValue(Event(error.message ?: ""))
    }

    fun isUserAuth() {
        cancelJob()
        viewModelCoroutineScope.launch {
            getDataFromInteractor()
        }
    }

    private suspend fun getDataFromInteractor(){
        withContext(dispatcherProvider.io()){
           val authUser: User? = interactor.isUserAuth()
            authUser?.let { _authUserLiveDate.postValue(Event(true)) }
                ?: _authUserLiveDate.postValue(Event(false))
        }
    }

    private fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    override fun alertDialogBtnOkClicked() {
        isUserAuth()
    }

}