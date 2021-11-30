package com.lenatopoleva.musicsearch.model.interactor

import com.lenatopoleva.musicsearch.model.data.AuthState

interface IAuthInteractor {
    suspend fun authUser(email: String, password: String): AuthState
}