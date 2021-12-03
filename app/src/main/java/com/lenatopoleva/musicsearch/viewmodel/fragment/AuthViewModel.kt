package com.lenatopoleva.musicsearch.viewmodel.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.fragment.IAuthInteractor
import com.lenatopoleva.musicsearch.navigation.Screens
import com.lenatopoleva.musicsearch.utils.Event
import com.lenatopoleva.musicsearch.utils.ui.TextValidator.Companion.EMAIL
import com.lenatopoleva.musicsearch.utils.ui.TextValidator.Companion.EMAIL_PATTERN
import com.lenatopoleva.musicsearch.utils.ui.TextValidator.Companion.PASSWORD
import com.lenatopoleva.musicsearch.utils.ui.TextValidator.Companion.PASSWORD_PATTERN
import com.lenatopoleva.musicsearch.viewmodel.base.BaseViewModel
import kotlinx.coroutines.*
import ru.terrakok.cicerone.Router
import java.util.regex.Matcher
import java.util.regex.Pattern

class AuthViewModel(
    private val dispatcherProvider: IDispatcherProvider,
    private val authInteractor: IAuthInteractor,
    private val router: Router
): BaseViewModel() {

    private val _emailValidationLiveData = MutableLiveData<Boolean>()
    val emailValidationLiveData : LiveData<Boolean> = _emailValidationLiveData

    private val _passwordValidationLiveData = MutableLiveData<Boolean>()
    val passwordValidationLiveData : LiveData<Boolean> = _passwordValidationLiveData

    private val _userNotRegistratedAlertDialogLiveData = MutableLiveData<Event<String>>()
    val userNotRegistratedAlertDialogLiveData : LiveData<Event<String>> = _userNotRegistratedAlertDialogLiveData

    val errorAlertDialogLiveData : LiveData<Event<String>> = _errorBaseLiveData

    private val _loadingLiveData = MutableLiveData<Event<String>>()
    val loadingLiveData : LiveData<Event<String>> = _loadingLiveData


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
            _loadingLiveData.postValue(Event(""))
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

    override fun backPressed(): Boolean {
        router.exit()
        return true
    }

}