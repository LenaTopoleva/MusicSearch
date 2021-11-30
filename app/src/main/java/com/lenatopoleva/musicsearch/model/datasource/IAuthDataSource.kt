package com.lenatopoleva.musicsearch.model.datasource

import com.lenatopoleva.musicsearch.model.data.entity.User

interface IAuthDataSource {
    suspend fun getAuthUser(): User?
}