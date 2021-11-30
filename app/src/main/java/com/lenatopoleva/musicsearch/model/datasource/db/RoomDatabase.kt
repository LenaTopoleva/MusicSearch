package com.lenatopoleva.musicsearch.model.datasource.db

import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.datasource.IAuthDataSource
import kotlinx.coroutines.delay
import java.lang.Exception

class RoomDatabase: IAuthDataSource {
    override suspend fun getAuthUser(): User? {
        delay(100)
        return User("Ivan", "Ivanov", 30, "", "", "")
//        throw Exception("my error")
    }
}