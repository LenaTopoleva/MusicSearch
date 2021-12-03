package com.lenatopoleva.musicsearch.viewmodel.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.fragment.IAlbumsInteractor
import com.lenatopoleva.musicsearch.navigation.Screens
import com.lenatopoleva.musicsearch.utils.Event
import com.lenatopoleva.musicsearch.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.terrakok.cicerone.Router

class AlbumsViewModel (private val albumsInteractor: IAlbumsInteractor,
                       private val router: Router,
                       private val dispatcherProvider: IDispatcherProvider,
): BaseViewModel() {

    private var _isAccountBtnClickedLiveData = MutableLiveData<Event<Boolean>>()
    val isAccountBtnClickedLiveData: LiveData<Event<Boolean>> = _isAccountBtnClickedLiveData

    private var _authUserLiveData = MutableLiveData<User>()
    val authUserLiveData: LiveData<User> = _authUserLiveData

    private var _authUserNotFoundLiveData = MutableLiveData<Boolean>()
    val authUserNotFoundLiveData: LiveData<Boolean> = _authUserNotFoundLiveData

    private var _logoutFailAlertDialogLiveData = MutableLiveData<Event<String>>()
    val logoutFailAlertDialogLiveData: LiveData<Event<String>> = _logoutFailAlertDialogLiveData
    
    private var _loaderLiveData = MutableLiveData<Boolean>()
    val loaderLiveData: LiveData<Boolean> = _loaderLiveData

    private var _mediaListLiveData = MutableLiveData<List<Media>>()
    val mediaListLiveData: LiveData<List<Media>> = _mediaListLiveData

    private var _noMediaAlertDialogLiveData = MutableLiveData<Event<String>>()
    val noMediaAlertDialogLiveData: LiveData<Event<String>> = _noMediaAlertDialogLiveData

    val errorLiveData: LiveData<Event<String>> = _errorBaseLiveData

    private var _bottomSheetErrorLiveData = MutableLiveData<String>()
    val bottomSheetErrorLiveData: LiveData<String> = _bottomSheetErrorLiveData

    private val _logoutProgressLiveData = MutableLiveData<Event<String>>()
    val logoutProgressLiveData : LiveData<Event<String>> = _logoutProgressLiveData


    fun getData(title: String, isOnline: Boolean) {
        _loaderLiveData.value = true
        cancelJob()
        viewModelCoroutineScope.launch {
            getDataFromInteractor(title, isOnline)
        }
    }

    private suspend fun getDataFromInteractor(title: String, isOnline: Boolean) {
        withContext(dispatcherProvider.io()) {
            val mediaList = albumsInteractor.getAlbumsByTitle(title, isOnline)
            _loaderLiveData.postValue(false)
            mediaList?.let {
                if (it.isEmpty()) {
                    _mediaListLiveData.postValue(it)
                    _noMediaAlertDialogLiveData.postValue(Event(""))
                } else {
                    _mediaListLiveData.postValue(it)
                }
            } ?: _noMediaAlertDialogLiveData.postValue(Event(""))
        }
    }

    fun logoutBtnClicked(email: String) {
        println("logoutBtnClicked")
        viewModelCoroutineScope.launch {
            logOut(email)
        }
    }

    private suspend fun logOut(email: String){
        withContext(dispatcherProvider.io()){
            _logoutProgressLiveData.postValue(Event(""))
            val doneSuccessful = albumsInteractor.logout(email)
            println("logout done")
            if (doneSuccessful) {
                println("logout success")
                openAuthFragment()
            }
            else {
                println("logout not success")
                _logoutFailAlertDialogLiveData.postValue(Event(""))
            }
        }
    }

    fun getAuthUser(){
        cancelJob()
        viewModelCoroutineScope.launch {
            getAuthUserFromInteractor()
        }
    }

    private suspend fun getAuthUserFromInteractor() {
        withContext(dispatcherProvider.io()){
            try {
                val authUser = albumsInteractor.getAuthUser()
                authUser?.let { _authUserLiveData.postValue(it) }
                    ?: _authUserNotFoundLiveData.postValue(true)
            } catch (error: Throwable){
                _bottomSheetErrorLiveData.postValue(error.message)
            }
        }
    }

    fun accountBtnClicked() {
        _isAccountBtnClickedLiveData.value = Event(true)
    }

    override fun handleError(error: Throwable) {
        _loaderLiveData.postValue(false)
        _errorBaseLiveData.postValue(Event(error.message ?: ""))
    }

    private suspend fun openAuthFragment(){
        withContext(dispatcherProvider.main()){
            router.replaceScreen(Screens.AuthScreen())
        }
    }

    override fun backPressed(): Boolean {
        router.exit()
        return true
    }

    fun albumClicked(albumId: Int) {
        albumId.let { router.navigateTo(Screens.AlbumDetailsScreen(it)) }
    }

}