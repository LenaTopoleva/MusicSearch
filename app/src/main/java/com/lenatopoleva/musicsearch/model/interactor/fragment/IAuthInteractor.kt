package com.lenatopoleva.musicsearch.model.interactor.fragment

import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.model.data.RegistrationState

interface IAuthInteractor {
    suspend fun authUser(email: String, password: String): AuthState
    suspend fun registerUser(name: String, surname: String, age: String,
                             phone: String, email: String, password: String): RegistrationState
}