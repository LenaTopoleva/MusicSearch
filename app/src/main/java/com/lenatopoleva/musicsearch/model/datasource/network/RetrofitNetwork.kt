package com.lenatopoleva.musicsearch.model.datasource.network

import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.datasource.IDataSource
import com.lenatopoleva.musicsearch.utils.mapToMediaList
import retrofit2.Retrofit

class RetrofitNetwork(private val retrofit: Retrofit): IDataSource {
    override suspend fun getAlbumsByTitle(title: String): List<Media>? {
        return getService().getAlbumsByTitle(albumTitle = title).await().mapToMediaList()
    }

    override suspend fun getAlbumDetailsById(id: Int): List<Media>? {
        return getService().getAlbumDetailsById(albumId = id).await().mapToMediaList()
    }

    private fun getService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    companion object {
        const val BASE_URL_LOCATIONS = "https://itunes.apple.com/"
    }

}