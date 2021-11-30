package com.lenatopoleva.musicsearch.model.interactor

import com.lenatopoleva.musicsearch.model.data.AuthState

interface ISplashInteractor {
    suspend fun isUserAuth(): AuthState
}