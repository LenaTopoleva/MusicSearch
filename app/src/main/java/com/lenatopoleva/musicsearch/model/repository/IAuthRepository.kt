package com.lenatopoleva.musicsearch.model.repository

import com.lenatopoleva.musicsearch.model.data.entity.User

interface IAuthRepository {

    suspend fun getAuthUser(): User?

    suspend fun authUser(email: String, password: String): User?

    suspend fun registerUser(name: String, surname: String, age: String,
                             phone: String, email: String, password: String): Boolean
}