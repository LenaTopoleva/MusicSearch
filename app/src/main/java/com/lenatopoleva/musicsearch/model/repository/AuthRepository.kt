package com.lenatopoleva.musicsearch.model.repository

import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.datasource.IAuthDataSource

class AuthRepository(private val dataSource: IAuthDataSource): IAuthRepository {

    override suspend fun getAuthUser(): User? =
        dataSource.getAuthUser()

}