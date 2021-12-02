package com.lenatopoleva.musicsearch.model.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lenatopoleva.musicsearch.model.datasource.db.entity.TrackDbEntity

@Dao
interface TracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tracks: List<TrackDbEntity>)

    @Query("SELECT * FROM tracks WHERE collectionId LIKE :id")
    suspend fun getTracksByAlbumId(id: Int): List<TrackDbEntity>?
}