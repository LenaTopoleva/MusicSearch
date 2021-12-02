package com.lenatopoleva.musicsearch.model.repository

import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.datasource.IDataSource
import java.util.*

class Repository (private val dataSource : IDataSource): IRepository {

    override suspend fun getAlbumsByTitle(title: String):  List<Media>? =
        dataSource.getAlbumsByTitle(title)?.sortedBy { it.collectionName.toLowerCase(Locale.ROOT) }


    override suspend fun getAlbumDetailsById(id: Int):  List<Media>? =
        dataSource.getAlbumDetailsById(id)


}