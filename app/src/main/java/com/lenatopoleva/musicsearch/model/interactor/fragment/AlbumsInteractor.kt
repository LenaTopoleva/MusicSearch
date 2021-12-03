package com.lenatopoleva.musicsearch.model.interactor.fragment

import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.repository.IAuthRepository
import com.lenatopoleva.musicsearch.model.repository.IRepository
import com.lenatopoleva.musicsearch.model.repository.IRepositoryLocal

class AlbumsInteractor (
    private val repositoryRemote: IRepository,
    private val repositoryLocal: IRepositoryLocal,
    private val repositoryAuth: IAuthRepository
): IAlbumsInteractor {

    override suspend fun getAlbumsByTitle(title: String, fromRemoteSource: Boolean): List<Media>? {
        val result: List<Media>?
        if (fromRemoteSource) {
            result = repositoryRemote.getAlbumsByTitle(title)
            repositoryLocal.saveAlbumsToDB(result)
        } else {
            result = repositoryLocal.getAlbumsByTitle(title)
        }
        return result
    }

    override suspend fun getAuthUser(): User? =
        repositoryAuth.getAuthUser()

    override suspend fun logout(email: String): Boolean =
        repositoryAuth.logout(email)

}