package com.lenatopoleva.musicsearch.model.repository

import com.lenatopoleva.musicsearch.model.data.Media

interface IRepositoryLocal : IRepository {
    suspend fun saveAlbumsToDB(media: List<Media>?)
    suspend fun saveAlbumTracksToDB(media: List<Media>?)
}