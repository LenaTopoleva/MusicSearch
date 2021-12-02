package com.lenatopoleva.musicsearch.model.datasource.network

import com.lenatopoleva.musicsearch.model.data.SearchResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search")
    fun getAlbumsByTitle(
        @Query("entity") entity: String = "album",
        @Query("attribute") attribute: String = "albumTerm",
        @Query("term") albumTitle: String): Deferred<SearchResponse>

    @GET("lookup")
    fun getAlbumDetailsById(
        @Query("entity") entity: String = "song",
        @Query("id") albumId: Int): Deferred<SearchResponse>

}
