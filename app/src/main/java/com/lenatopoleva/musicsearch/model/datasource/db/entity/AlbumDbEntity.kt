package com.lenatopoleva.musicsearch.model.datasource.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "albums",
    indices = [Index(value = ["collectionName"], unique = false),
        Index(value = ["collectionId"], unique = true)])
data class AlbumDbEntity (
    @PrimaryKey
    val collectionId : Int,
    val artistName : String,
    val collectionName : String,
    val artworkUrl100 : String,
    val country : String,
    val releaseDate : String,
    val primaryGenreName : String,
)