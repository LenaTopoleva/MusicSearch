package com.lenatopoleva.musicsearch.viewmodel.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.IAlbumsInteractor
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
    val isAccountBtnClickedLiveData: LiveData<Event<Boolean>>
        get() = _isAccountBtnClickedLiveData

    private var _authUserLiveData = MutableLiveData<AuthState>()
    val authUserLiveData: LiveData<AuthState>
        get() = _authUserLiveData

    private var _logoutFailAlertDialogLiveData = MutableLiveData<Event<String>>()
    val logoutFailAlertDialogLiveData: LiveData<Event<String>>
        get() = _logoutFailAlertDialogLiveData
    
    private var _loaderLiveData = MutableLiveData<Boolean>()
    val loaderLiveData: LiveData<Boolean>
        get() = _loaderLiveData

    private var _mediaListLiveData = MutableLiveData<List<Media>>()
    val mediaListLiveData: LiveData<List<Media>>
        get() = _mediaListLiveData

    private var _noMediaAlertDialogLiveData = MutableLiveData<Event<String>>()
    val noMediaAlertDialogLiveData: LiveData<Event<String>>
        get() = _noMediaAlertDialogLiveData

    val errorLiveData: LiveData<Event<String>>
        get() = _errorLiveData

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
            _authUserLiveData.postValue(albumsInteractor.getAuthUser())
        }
    }

    fun accountBtnClicked() {
        _isAccountBtnClickedLiveData.value = Event(true)
    }

    override fun handleError(error: Throwable) {
        _loaderLiveData.postValue(false)
        _errorLiveData.postValue(Event(error.message ?: ""))
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