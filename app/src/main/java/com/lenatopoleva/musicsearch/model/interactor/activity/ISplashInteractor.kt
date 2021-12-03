package com.lenatopoleva.musicsearch.model.interactor.activity

import com.lenatopoleva.musicsearch.model.data.entity.User

interface ISplashInteractor {
    suspend fun isUserAuth(): User?
}