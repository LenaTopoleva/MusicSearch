package com.lenatopoleva.musicsearch.model.interactor.activity

import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.repository.IAuthRepository

class SplashInteractor(
    private val authRepository: IAuthRepository
): ISplashInteractor {

    override suspend fun isUserAuth(): User? = authRepository.getAuthUser()

}