package com.lenatopoleva.musicsearch.model.interactor.activity

import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.model.repository.IAuthRepository

class SplashInteractor(
    private val authRepository: IAuthRepository
): ISplashInteractor {

    override suspend fun isUserAuth(): AuthState =
        AuthState.Success(authRepository.getAuthUser())

}