package com.lenatopoleva.musicsearch.model.interactor.activity

import com.lenatopoleva.musicsearch.model.data.AuthState

interface ISplashInteractor {
    suspend fun isUserAuth(): AuthState
}