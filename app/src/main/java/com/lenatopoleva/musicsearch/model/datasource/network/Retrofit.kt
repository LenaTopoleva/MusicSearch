package com.lenatopoleva.musicsearch.model.datasource.network

import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.datasource.IDataSource
import kotlinx.coroutines.delay

class Retrofit: IDataSource {
    override suspend fun getAlbumsByTitle(title: String): List<Media>? {
        delay(300)
        return null
    }

    override suspend fun getAlbumDetailsById(id: Int): List<Media>? {
        delay(300)
        return null
    }
}