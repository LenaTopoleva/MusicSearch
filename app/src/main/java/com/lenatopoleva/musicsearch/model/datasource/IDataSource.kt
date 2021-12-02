package com.lenatopoleva.musicsearch.model.datasource

import com.lenatopoleva.musicsearch.model.data.Media


interface IDataSource {
    suspend fun getAlbumsByTitle(title: String):  List<Media>?
    suspend fun getAlbumDetailsById(id: Int):  List<Media>?
}