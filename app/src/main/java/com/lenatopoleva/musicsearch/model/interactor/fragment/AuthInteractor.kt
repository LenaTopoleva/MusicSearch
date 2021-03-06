package com.lenatopoleva.musicsearch.model.interactor.fragment

import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.repository.IAuthRepository

class AuthInteractor(
    private val authRepository: IAuthRepository
): IAuthInteractor {

    override suspend fun authUser(email: String, password: String): User? =
        authRepository.authUser(email, password)

    override suspend fun registerUser(name: String, surname: String, age: String,
        phone: String, email: String, password: String): Boolean =
        authRepository.registerUser(name, surname, age, phone, email, password)
}