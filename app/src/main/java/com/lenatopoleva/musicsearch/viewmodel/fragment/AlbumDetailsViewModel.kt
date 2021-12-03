package com.lenatopoleva.musicsearch.viewmodel.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.fragment.IAlbumDetailsInteractor
import com.lenatopoleva.musicsearch.utils.Event
import com.lenatopoleva.musicsearch.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.terrakok.cicerone.Router

class AlbumDetailsViewModel (private val albumDetailsAlbumsInteractor: IAlbumDetailsInteractor,
                             private val router: Router,
                             private val dispatcherProvider: IDispatcherProvider
): BaseViewModel() {

    private var _loaderLiveData = MutableLiveData<Boolean>()
    val loaderLiveData: LiveData<Boolean> = _loaderLiveData

    private var _mediaListLiveData = MutableLiveData<List<Media>>()
    val mediaListLiveData: LiveData<List<Media>> = _mediaListLiveData

    private var _noMediaAlertDialogLiveData = MutableLiveData<Event<String>>()
    val noMediaAlertDialogLiveData: LiveData<Event<String>> = _noMediaAlertDialogLiveData

    val errorLiveData: LiveData<Event<String>> = _errorBaseLiveData

    fun getData(albumId: Int, isOnline: Boolean) {
        _loaderLiveData.value = true
        cancelJob()
        viewModelCoroutineScope.launch {
            getDataFromInteractor(albumId, isOnline)
        }
    }

    private suspend fun getDataFromInteractor(albumId: Int, isOnline: Boolean) {
        withContext(dispatcherProvider.io()){
            val mediaList = (albumDetailsAlbumsInteractor.getAlbumTracksById(albumId, isOnline))
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

    override fun handleError(error: Throwable) {
        _loaderLiveData.postValue(false)
        _errorBaseLiveData.postValue(Event(error.message ?: ""))
    }

    override fun backPressed(): Boolean {
        router.exit()
        return true
    }

}