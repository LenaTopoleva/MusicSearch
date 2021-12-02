package com.lenatopoleva.musicsearch.model.interactor

import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.model.data.Media

interface IAlbumsInteractor {
    suspend fun getAlbumsByTitle(title: String, fromRemoteSource: Boolean): List<Media>?
    suspend fun getAuthUser(): AuthState
    suspend fun logout(email: String): Boolean
}