package com.lenatopoleva.musicsearch.viewmodel.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lenatopoleva.musicsearch.model.data.RegistrationState
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.fragment.IAuthInteractor
import com.lenatopoleva.musicsearch.navigation.Screens
import com.lenatopoleva.musicsearch.utils.ui.TextValidator
import kotlinx.coroutines.*
import ru.terrakok.cicerone.Router
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistrationViewModel(
    private val dispatcherProvider: IDispatcherProvider,
    private val authInteractor: IAuthInteractor,
    private val router: Router
): ViewModel() {

    private val _nameValidationLiveData = MutableLiveData<Boolean>()
    val nameValidationLiveData : LiveData<Boolean> = _nameValidationLiveData

    private val _surnameValidationLiveData = MutableLiveData<Boolean>()
    val surnameValidationLiveData : LiveData<Boolean> = _surnameValidationLiveData

    private val _ageValidationLiveData = MutableLiveData<Boolean>()
    val ageValidationLiveData : LiveData<Boolean> = _ageValidationLiveData

    private val _phoneValidationLiveData = MutableLiveData<Boolean>()
    val phoneValidationLiveData : LiveData<Boolean> = _phoneValidationLiveData

    private val _emailValidationLiveData = MutableLiveData<Boolean>()
    val emailValidationLiveData : LiveData<Boolean> = _emailValidationLiveData

    private val _passwordValidationLiveData = MutableLiveData<Boolean>()
    val passwordValidationLiveData : LiveData<Boolean> = _passwordValidationLiveData

    private val _birthDateLiveData = MutableLiveData<String>()
    val birthDateLiveData : LiveData<String> = _birthDateLiveData

    private val _registrationStateLiveData = MutableLiveData<RegistrationState>()
    val registrationStateLiveData : LiveData<RegistrationState> = _registrationStateLiveData

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
        _registrationStateLiveData.postValue(RegistrationState.Error(error))
    }

    fun validate(text: String, type: String) {
        when(type){
            TextValidator.NAME -> _nameValidationLiveData.value = isValid(text, TextValidator.NAME_PATTERN)
            TextValidator.SURNAME -> _surnameValidationLiveData.value = isValid(text, TextValidator.SURNAME_PATTERN)
            TextValidator.AGE -> _ageValidationLiveData.value = isAgeValid(text)
            TextValidator.PHONE -> _phoneValidationLiveData.value = isValid(text, TextValidator.PHONE_PATTERN)
            TextValidator.EMAIL -> _emailValidationLiveData.value = isValid(text, TextValidator.EMAIL_PATTERN)
            TextValidator.PASSWORD -> _passwordValidationLiveData.value = isValid(text, TextValidator.PASSWORD_PATTERN)
        }
    }

    private fun isValid (text: String, patternType: String): Boolean {
        val pattern: Pattern = Pattern.compile(patternType)
        val matcher: Matcher = pattern.matcher(text)
        return (text.isNotEmpty() && matcher.matches())
    }

    private fun isAgeValid(text: String): Boolean =
        (isValid(text, TextValidator.AGE_PATTERN) && isPersonOverEighteen(text))

    private fun isPersonOverEighteen(date: String): Boolean{
        try {
            val userBirthDay = date.subSequence(0, 2).toString().toInt()
            val userBirthMonth = date.subSequence(3, 5).toString().toInt()
            val userBirthYear = date.subSequence(6, 10).toString().toInt()

            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val currentDay = sdf.format(Date()).subSequence(0, 2).toString().toInt()
            val currentMonth = sdf.format(Date()).subSequence(3, 5).toString().toInt()
            val currentYear = sdf.format(Date()).subSequence(6, 10).toString().toInt()

            return compareDates(currentYear, userBirthYear, currentMonth, userBirthMonth,
                currentDay, userBirthDay)

        } catch (ex: NumberFormatException) {
            return false
        }
    }

    private fun compareDates(currentYear: Int, userBirthYear: Int,
                             currentMonth: Int, userBirthMonth: Int,
                             currentDay: Int, userBirthDay: Int): Boolean {
        return when {
            currentYear - userBirthYear < 18 -> false
            currentYear - userBirthYear > 18 -> true
            else -> compareMonths(currentMonth, userBirthMonth, currentDay, userBirthDay)
        }
    }

    private fun compareMonths(currentMonth: Int, userBirthMonth: Int,
                              currentDay: Int, userBirthDay: Int): Boolean {
        return when {
            currentMonth < userBirthMonth -> false
            currentMonth > userBirthMonth -> true
            else -> compareDays(currentDay, userBirthDay)
        }
    }

    private fun compareDays(currentDay: Int, userBirthDay: Int): Boolean {
        return when {
            currentDay < userBirthDay -> false
            else -> return true
        }
    }

    fun datePickerPositiveBtnClicked(date: String) {
        _birthDateLiveData.value = date
    }

    fun btnSignUpClicked(name: String, surname: String, age: String, phone: String, email: String, password: String) {
        if (_nameValidationLiveData.value == true &&
            _surnameValidationLiveData.value == true &&
            _ageValidationLiveData.value == true &&
            _phoneValidationLiveData.value == true &&
            _emailValidationLiveData.value == true &&
            _passwordValidationLiveData.value == true){
            cancelJob()
            viewModelCoroutineScope.launch {
                registerUser(name, surname, age, phone, email, password)
            }
        }
    }

    private suspend fun registerUser(name: String, surname: String, age: String,
                                     phone: String, email: String, password: String){
        withContext(dispatcherProvider.io()){
            _registrationStateLiveData.postValue(authInteractor.registerUser(name, surname, age, phone, email, password))
        }
    }

    fun userIsRegistrated() {
        router.replaceScreen(Screens.AuthScreen())
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