package com.lenatopoleva.musicsearch.utils

import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.datasource.db.entity.AlbumDbEntity
import com.lenatopoleva.musicsearch.model.datasource.db.entity.TrackDbEntity

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

suspend fun convertListMediaToAlbumDbEntities(list: List<Media>?): List<AlbumDbEntity>? =
    list?.map {
        AlbumDbEntity(
            collectionId = it.collectionId,
            artistName = it.artistName,
            collectionName = it.collectionName,
            artworkUrl100 = it.artworkUrl100,
            country = it.country,
            releaseDate = it.releaseDate,
            primaryGenreName = it.primaryGenreName)
    }

suspend fun convertAppStateDataToTrackDbEntities(list: List<Media>?): List<TrackDbEntity>? =
    list?.filterNot { it.wrapperType == COLLECTION }?.map {
        TrackDbEntity(
            collectionId = it.collectionId,
            artistName = it.artistName,
            collectionName = it.collectionName,
            artworkUrl100 = it.artworkUrl100,
            country = it.country,
            releaseDate = it.releaseDate,
            primaryGenreName = it.primaryGenreName,
            trackName = it.trackName ?: "",
            trackNumber = it.trackNumber ?: 0,
            trackTimeMillis = it.trackTimeMillis ?: 0
        )
    }

suspend fun mapAlbumDbEntityListToMediaList(albumDbEntities: List<AlbumDbEntity>) : List<Media> =
    albumDbEntities.map {
        Media(
            wrapperType = COLLECTION,
            collectionId = it.collectionId,
            artistName = it.artistName,
            collectionName = it.collectionName,
            artworkUrl100 = it.artworkUrl100,
            country = it.country,
            releaseDate = it.releaseDate,
            primaryGenreName = it.primaryGenreName)
    }

suspend fun mapTrackDbEntityListToMediaList(trackDbEntities: List<TrackDbEntity>) : List<Media> =
    trackDbEntities.map {
        Media(
            wrapperType = TRACK,
            collectionId = it.collectionId,
            artistName = it.artistName,
            collectionName = it.collectionName,
            artworkUrl100 = it.artworkUrl100,
            country = it.country,
            releaseDate = it.releaseDate,
            primaryGenreName = it.primaryGenreName,
            trackName = it.trackName ?: "",
            trackNumber = it.trackNumber ?: 0,
            trackTimeMillis = it.trackTimeMillis ?: 0)
    }

