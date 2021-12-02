package com.lenatopoleva.musicsearch.model.data

data class SearchResponse (
    val resultCount: Long,
    val results: List<Media>
)
