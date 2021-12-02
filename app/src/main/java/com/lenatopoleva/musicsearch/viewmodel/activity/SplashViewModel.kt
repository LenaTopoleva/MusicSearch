package com.lenatopoleva.musicsearch.viewmodel.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.activity.ISplashInteractor
import com.lenatopoleva.musicsearch.utils.ui.alertdialog.AlertDialogListener
import kotlinx.coroutines.*

class SplashViewModel(private val interactor: ISplashInteractor,
                      private val dispatcherProvider: IDispatcherProvider
): ViewModel(), AlertDialogListener {

    private val _authStateLiveData = MutableLiveData<AuthState>()

    private val authStateLiveData: LiveData<AuthState>
        get() = _authStateLiveData

    fun subscribe(): LiveData<AuthState> {
        return authStateLiveData
    }

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })

    private fun handleError(error: Throwable) {
        _authStateLiveData.value = (AuthState.Error(error))
    }

    fun isUserAuth() {
        cancelJob()
        viewModelCoroutineScope.launch {
            getDataFromInteractor()
        }
    }

    private suspend fun getDataFromInteractor(){
        withContext(dispatcherProvider.io()){
            _authStateLiveData.postValue(interactor.isUserAuth())
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