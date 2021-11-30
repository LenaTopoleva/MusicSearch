package com.lenatopoleva.musicsearch.model.repository

import com.lenatopoleva.musicsearch.model.data.entity.User

interface IAuthRepository {

    suspend fun getAuthUser(): User?

    suspend fun authUser(email: String, password: String): User?
}