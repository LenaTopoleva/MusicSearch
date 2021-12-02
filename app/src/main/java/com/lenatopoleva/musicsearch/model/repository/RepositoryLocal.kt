package com.lenatopoleva.musicsearch.model.repository

import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.datasource.IDataSourceLocal
import java.util.*

class RepositoryLocal(private val dataSource: IDataSourceLocal) : IRepositoryLocal {

    override suspend fun saveAlbumsToDB(media: List<Media>?) {
        dataSource.saveAlbumsToDB(media)
    }

    override suspend fun saveAlbumTracksToDB(media: List<Media>?) {
        dataSource.saveAlbumTracksToDB(media)
    }

    override suspend fun getAlbumsByTitle(title: String):  List<Media>? =
        dataSource.getAlbumsByTitle(title)?.sortedBy { it.collectionName.toLowerCase(Locale.ROOT) }

    override suspend fun getAlbumDetailsById(id: Int):  List<Media>? =
        dataSource.getAlbumDetailsById(id)

}