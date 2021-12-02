package com.lenatopoleva.musicsearch.model.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.datasource.db.entity.UserDbEntity

@Dao
interface UsersDao {
    @Query("SELECT * FROM users WHERE auth = 'true'")
    suspend fun getAuthUser(): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserWithEmail(email: String): User?

    @Query("UPDATE users SET auth = 'true' WHERE email = :email")
    suspend fun authUser(email: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerUser(user: UserDbEntity)

    @Query("UPDATE users SET auth = 'false' WHERE email = :email")
    suspend fun logout(email: String)
}