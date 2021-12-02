package com.lenatopoleva.musicsearch.model.datasource.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tracks",
    indices = [Index(value = ["collectionId"], unique = false)],
)
data class TrackDbEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val collectionId : Int,
    val artistName : String,
    val collectionName : String,
    val artworkUrl100 : String,
    val country : String,
    val releaseDate : String,
    val primaryGenreName : String,
    val trackName: String,
    val trackNumber: Int,
    val trackTimeMillis: Long = 0
)