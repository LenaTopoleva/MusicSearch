package com.lenatopoleva.musicsearch.model.interactor

import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.model.data.RegistrationState
import com.lenatopoleva.musicsearch.model.repository.IAuthRepository

class AuthInteractor(
    private val authRepository: IAuthRepository
): IAuthInteractor {

    override suspend fun authUser(email: String, password: String): AuthState {
            return AuthState.Success(authRepository.authUser(email, password))
    }

    override suspend fun registerUser(name: String, surname: String, age: String,
        phone: String, email: String, password: String): RegistrationState {
        return RegistrationState.Success(authRepository.registerUser(name, surname, age, phone, email, password))
    }
}