package com.lenatopoleva.musicsearch.utils

import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.data.SearchResponse
import com.lenatopoleva.musicsearch.model.data.entity.Album
import com.lenatopoleva.musicsearch.model.data.entity.Track

fun SearchResponse.mapToMediaList(): List<Media> {
    return this.results
}

fun Media.mapToAlbum(): Album {
    return Album(
        artistId = this.artistId,
        collectionId = this.collectionId,
        artistName = this.artistName,
        collectionName = this.collectionName,
        artworkUrl100 = this.artworkUrl100,
        trackCount = this.trackCount,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        releaseDate = this.releaseDate
    )
}

fun Media.mapToTrack(): Track {
    return Track(
        artistId = this.artistId,
        collectionId = this.collectionId,
        artistName = this.artistName,
        collectionName = this.collectionName,
        artworkUrl100 = this.artworkUrl100,
        trackName = this.trackName ?: "",
        trackNumber = this.trackNumber ?: 0,
        trackTimeMillis = this.trackTimeMillis ?: 0
    )
}

fun List<Media>.filterFromCollections(): List<Media> = this.filterNot { it.wrapperType == COLLECTION }