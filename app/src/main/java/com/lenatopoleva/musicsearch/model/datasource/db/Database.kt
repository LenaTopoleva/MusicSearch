package com.lenatopoleva.musicsearch.model.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lenatopoleva.musicsearch.model.datasource.db.dao.AlbumsDao
import com.lenatopoleva.musicsearch.model.datasource.db.dao.TracksDao
import com.lenatopoleva.musicsearch.model.datasource.db.dao.UsersDao
import com.lenatopoleva.musicsearch.model.datasource.db.entity.AlbumDbEntity
import com.lenatopoleva.musicsearch.model.datasource.db.entity.TrackDbEntity
import com.lenatopoleva.musicsearch.model.datasource.db.entity.UserDbEntity

@Database(entities = arrayOf(UserDbEntity::class, AlbumDbEntity::class, TrackDbEntity::class),
    version = 1,
    exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun albumsDao(): AlbumsDao
    abstract fun tracksDao(): TracksDao
}