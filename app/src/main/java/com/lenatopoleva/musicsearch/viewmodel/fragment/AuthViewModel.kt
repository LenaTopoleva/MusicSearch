package com.lenatopoleva.musicsearch.viewmodel.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.fragment.IAuthInteractor
import com.lenatopoleva.musicsearch.navigation.Screens
import com.lenatopoleva.musicsearch.utils.ui.TextValidator.Companion.EMAIL
import com.lenatopoleva.musicsearch.utils.ui.TextValidator.Companion.EMAIL_PATTERN
import com.lenatopoleva.musicsearch.utils.ui.TextValidator.Companion.PASSWORD
import com.lenatopoleva.musicsearch.utils.ui.TextValidator.Companion.PASSWORD_PATTERN
import kotlinx.coroutines.*
import ru.terrakok.cicerone.Router
import java.util.regex.Matcher
import java.util.regex.Pattern

class AuthViewModel(
    private val dispatcherProvider: IDispatcherProvider,
    private val authInteractor: IAuthInteractor,
    private val router: Router
): ViewModel() {

    private val _emailValidationLiveData = MutableLiveData<Boolean>()
    private val _passwordValidationLiveData = MutableLiveData<Boolean>()
    private val _authStateLiveData = MutableLiveData<AuthState>()

    val emailValidationLiveData : LiveData<Boolean>
        get() = _emailValidationLiveData

    val passwordValidationLiveData : LiveData<Boolean>
        get() = _passwordValidationLiveData

    val authStateLiveData : LiveData<AuthState>
        get() = _authStateLiveData

    protected val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })

    private fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    private fun handleError(error: Throwable){
        _authStateLiveData.postValue(AuthState.Error(error))
    }

    fun validate(text: String, type: String) {
        when(type){
            EMAIL ->
                _emailValidationLiveData.value =
                    isValid(text, EMAIL_PATTERN)
            PASSWORD -> _passwordValidationLiveData.value =
                isValid(text, PASSWORD_PATTERN)
        }
    }

    private fun isValid (text: String, patternType: String): Boolean {
        val pattern: Pattern = Pattern.compile(patternType)
        val matcher: Matcher = pattern.matcher(text)
        return (text.isNotEmpty() && matcher.matches())
    }

    fun btnLogInClicked(email: String, password: String) {
        if (_emailValidationLiveData.value == true &&
            _passwordValidationLiveData.value == true){
            cancelJob()
            viewModelCoroutineScope.launch {
                authenticateUser(email, password)
            }
        }
    }

    private suspend fun authenticateUser(email: String, password: String){
        withContext(dispatcherProvider.io()){
            _authStateLiveData.postValue(authInteractor.authUser(email, password))
        }
    }


    fun userIsAuth() {
        router.replaceScreen(Screens.AlbumsScreen())
    }

    fun btnRegistrationClicked() {
        router.replaceScreen(Screens.RegistrationScreen())
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

}