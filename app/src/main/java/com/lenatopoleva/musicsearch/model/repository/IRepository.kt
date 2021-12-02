package com.lenatopoleva.musicsearch.model.repository

import com.lenatopoleva.musicsearch.model.data.Media

interface IRepository {
    suspend fun getAlbumsByTitle(title: String): List<Media>?
    suspend fun getAlbumDetailsById(id: Int): List<Media>?
}