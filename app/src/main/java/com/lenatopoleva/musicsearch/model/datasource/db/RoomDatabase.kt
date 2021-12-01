package com.lenatopoleva.musicsearch.model.datasource.db

import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.datasource.IAuthDataSource
import kotlinx.coroutines.delay

class RoomDatabase: IAuthDataSource {
    override suspend fun getAuthUser(): User? {
        delay(100)
        println("RoomDatabase getAuthUser")
//        return User("Ivan", "Ivanov", 30, "", "", "")
//        throw Exception("my error")
        return null
    }

    override suspend fun authUser(email: String, password: String): User? {
        delay(200)
        return User("Ivan", "Ivanov", 30, "", "", "")
//        return null
    }

    override suspend fun registerUser(name: String, surname: String, age: String,
        phone: String, email: String, password: String): Boolean {
        delay(1000)
        return true
    }
}