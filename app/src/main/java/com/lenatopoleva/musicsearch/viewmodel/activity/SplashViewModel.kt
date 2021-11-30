package com.lenatopoleva.musicsearch.viewmodel.activity

import androidx.lifecycle.ViewModel
import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.ISplashInteractor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SplashViewModel(private val interactor: ISplashInteractor,
                      private val dispatcherProvider: IDispatcherProvider
): ViewModel() {

    private val initialState: AuthState = AuthState.Initial

    private val mutableAuthStateFlow: MutableStateFlow<AuthState> by lazy {
        MutableStateFlow(initialState)
    }

    private val authStateFlow: StateFlow<AuthState> = mutableAuthStateFlow.asStateFlow()

    fun subscribe(): StateFlow<AuthState> {
        return authStateFlow
    }

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })

    private fun handleError(error: Throwable) {
        mutableAuthStateFlow.value = (AuthState.Error(error))
    }

    fun isUserAuth() {
        cancelJob()
        viewModelCoroutineScope.launch {
            getDataFromInteractor()
        }
    }

    private suspend fun getDataFromInteractor(){
        withContext(dispatcherProvider.io()){
            mutableAuthStateFlow.value = interactor.isUserAuth()
        }
    }

    private fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    fun alertBtnOkClicked() {
        isUserAuth()
    }

}