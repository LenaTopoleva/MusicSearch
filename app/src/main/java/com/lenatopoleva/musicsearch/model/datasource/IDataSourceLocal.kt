package com.lenatopoleva.musicsearch.model.datasource

import com.lenatopoleva.musicsearch.model.data.Media

interface IDataSourceLocal : IDataSource {
    suspend fun saveAlbumsToDB(media: List<Media>?)
    suspend fun saveAlbumTracksToDB(media: List<Media>?)
}