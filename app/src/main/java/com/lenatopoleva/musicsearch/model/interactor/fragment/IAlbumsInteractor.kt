package com.lenatopoleva.musicsearch.model.interactor.fragment

import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.data.entity.User

interface IAlbumsInteractor {
    suspend fun getAlbumsByTitle(title: String, fromRemoteSource: Boolean): List<Media>?
    suspend fun getAuthUser(): User?
    suspend fun logout(email: String): Boolean
}