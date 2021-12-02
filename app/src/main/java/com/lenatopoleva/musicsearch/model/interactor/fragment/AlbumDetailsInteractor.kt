package com.lenatopoleva.musicsearch.model.interactor.fragment

import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.repository.IRepository
import com.lenatopoleva.musicsearch.model.repository.IRepositoryLocal

class AlbumDetailsInteractor(
    private val repositoryRemote: IRepository,
    private val repositoryLocal: IRepositoryLocal
): IAlbumDetailsInteractor {

    override suspend fun getAlbumTracksById(albumId: Int, fromRemoteSource: Boolean): List<Media>? {
        val result: List<Media>?
        if (fromRemoteSource) {
            result = repositoryRemote.getAlbumDetailsById(albumId)
            repositoryLocal.saveAlbumTracksToDB(result)
        } else {
            result = repositoryLocal.getAlbumDetailsById(albumId)
        }
        return result
    }

}