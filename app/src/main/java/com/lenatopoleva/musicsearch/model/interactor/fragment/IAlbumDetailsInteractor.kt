package com.lenatopoleva.musicsearch.model.interactor.fragment

import com.lenatopoleva.musicsearch.model.data.Media

interface IAlbumDetailsInteractor {
    suspend fun getAlbumTracksById(albumId: Int, fromRemoteSource: Boolean): List<Media>?
}