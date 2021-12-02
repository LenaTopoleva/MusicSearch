package com.lenatopoleva.musicsearch.model.imageloader

interface IImageLoader<T> {
    fun loadInto(url: String, container: T)
}