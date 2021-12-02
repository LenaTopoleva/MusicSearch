package com.lenatopoleva.musicsearch.model.datasource.db

import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.datasource.IAuthDataSource
import com.lenatopoleva.musicsearch.model.datasource.IDataSourceLocal
import kotlinx.coroutines.delay

class RoomDatabase: IAuthDataSource, IDataSourceLocal {

    override suspend fun getAuthUser(): User? {
        delay(100)
        println("RoomDatabase getAuthUser")
        return User("Ivan", "Ivanov", "30", "", "", "")
//        throw Exception("my error")
//        return null
    }

    override suspend fun authUser(email: String, password: String): User? {
        delay(200)
        return User("Ivan", "Ivanov", "30", "", "", "")
//        return null
    }

    override suspend fun registerUser(name: String, surname: String, age: String,
        phone: String, email: String, password: String): Boolean {
        delay(1000)
        return true
        //return false if user with this email address is already registered
    }

    override suspend fun logout(email: String): Boolean {
        delay(200)
//        throw Exception("my big exception")
        return false
    }

    override suspend fun saveAlbumsToDB(media: List<Media>?) {
        //save
    }

    override suspend fun saveAlbumTracksToDB(media: List<Media>?) {
        //save
    }

    override suspend fun getAlbumsByTitle(title: String): List<Media>? {
        delay(300)
        return null
    }

    override suspend fun getAlbumDetailsById(id: Int): List<Media>? {
        delay(300)
        return null
    }
}