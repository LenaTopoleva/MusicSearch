package com.lenatopoleva.musicsearch.viewmodel.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.fragment.IAuthInteractor
import com.lenatopoleva.musicsearch.navigation.Screens
import com.lenatopoleva.musicsearch.utils.Event
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
    val emailValidationLiveData : LiveData<Boolean> = _emailValidationLiveData

    private val _passwordValidationLiveData = MutableLiveData<Boolean>()
    val passwordValidationLiveData : LiveData<Boolean> = _passwordValidationLiveData

    private val _userNotRegistratedAlertDialogLiveData = MutableLiveData<Event<String>>()
    val userNotRegistratedAlertDialogLiveData : LiveData<Event<String>> = _userNotRegistratedAlertDialogLiveData

    private val _errorAlertDialogLiveData = MutableLiveData<Event<String>>()
    val errorAlertDialogLiveData : LiveData<Event<String>> = _errorAlertDialogLiveData

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
        _errorAlertDialogLiveData.postValue(Event(error.message ?: ""))
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
            val authUser: User? = authInteractor.authUser(email, password)
            authUser?.let {
                withContext(dispatcherProvider.main()){ userIsAuth() }
            } ?: _userNotRegistratedAlertDialogLiveData.postValue(Event(""))
        }
    }

    private fun userIsAuth() {
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