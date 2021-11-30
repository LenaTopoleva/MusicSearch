package com.lenatopoleva.musicsearch.model.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface IDispatcherProvider {
    fun io(): CoroutineDispatcher
}