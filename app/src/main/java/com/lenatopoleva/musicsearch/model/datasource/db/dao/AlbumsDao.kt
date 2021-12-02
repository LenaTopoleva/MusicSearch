package com.lenatopoleva.musicsearch.model.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lenatopoleva.musicsearch.model.datasource.db.entity.AlbumDbEntity

@Dao
interface AlbumsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(albumEntities: List<AlbumDbEntity>)

    @Query("SELECT * FROM albums WHERE collectionName LIKE '%' || :title || '%'")
    suspend fun getAlbumsByTitle(title: String): List<AlbumDbEntity>?

    @Query("SELECT * FROM albums WHERE collectionId LIKE :id")
    suspend fun getAlbumById(id: Int): AlbumDbEntity?
}