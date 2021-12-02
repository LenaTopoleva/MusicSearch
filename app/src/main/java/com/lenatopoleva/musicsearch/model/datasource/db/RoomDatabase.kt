package com.lenatopoleva.musicsearch.model.datasource.db

import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2KtResult
import com.lambdapioneer.argon2kt.Argon2Mode
import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.model.datasource.IAuthDataSource
import com.lenatopoleva.musicsearch.model.datasource.IDataSourceLocal
import com.lenatopoleva.musicsearch.model.datasource.db.dao.AlbumsDao
import com.lenatopoleva.musicsearch.model.datasource.db.dao.TracksDao
import com.lenatopoleva.musicsearch.model.datasource.db.dao.UsersDao
import com.lenatopoleva.musicsearch.model.datasource.db.entity.AlbumDbEntity
import com.lenatopoleva.musicsearch.model.datasource.db.entity.UserDbEntity
import com.lenatopoleva.musicsearch.utils.*

class RoomDatabase(private val usersDao: UsersDao,
                   private val albumsDao: AlbumsDao,
                   private val tracksDao: TracksDao,
                   private val argon2Kt: Argon2Kt
): IAuthDataSource, IDataSourceLocal {

    private suspend fun generatePasswordHash(password: String): String {
        //generate salt
        val salt = getRandomString(8)

        // hash a password
        val hashResult: Argon2KtResult = argon2Kt.hash(
            mode = Argon2Mode.ARGON2_I,
            password = password.toByteArray(),
            salt = salt.toByteArray(),
            tCostInIterations = 5,
            mCostInKibibyte = 65536
        )

        println("hashResult: $hashResult")
        println("Raw hash: ${hashResult.rawHashAsHexadecimal()}")
        println("Encoded string: ${hashResult.encodedOutputAsString()}")

        return hashResult.encodedOutputAsString()
    }

    fun passwordsAreEqual (password: String, encodedPassword: String): Boolean{
        // verify a password against an encoded string representation
        val verificationResult : Boolean = argon2Kt.verify(
            mode = Argon2Mode.ARGON2_I,
            encoded = encodedPassword,
            password = password.toByteArray()
        )
        return verificationResult
    }

    override suspend fun getAuthUser(): User? =
        usersDao.getAuthUser()


    override suspend fun authUser(email: String, password: String): User? {
        val user: User? = usersDao.getUserWithEmail(email)
        user?.let {
            if (passwordsAreEqual(password, user.password)) {
                usersDao.authUser(email)
                return user
            }
        }
        return null
    }

    override suspend fun registerUser(name: String, surname: String, birthday: String,
        phone: String, email: String, password: String): Boolean {
        val userWithSameEmail: User? = usersDao.getUserWithEmail(email)
        if (userWithSameEmail != null) return false
        val encodedPassword = generatePasswordHash(password)
        usersDao.registerUser(UserDbEntity(name = name, surname = surname, birthday = birthday,
            phone = phone, email =  email, password = encodedPassword))
        return true
    }

    override suspend fun logout(email: String): Boolean {
        val currentUser: User? = usersDao.getAuthUser()
        currentUser?.let {
            usersDao.logout(currentUser.email)
            return true
        } ?: return false
    }

    override suspend fun saveAlbumsToDB(media: List<Media>?) {
        convertListMediaToAlbumDbEntities(media)?.let{
            albumsDao.insertAll(it)
        }
    }

    override suspend fun saveAlbumTracksToDB(media: List<Media>?) {
        convertAppStateDataToTrackDbEntities(media)?.let{
            tracksDao.insertAll(it)
        }
    }

    override suspend fun getAlbumsByTitle(title: String): List<Media>? {
        if (title == "") return null
        val albums: List<AlbumDbEntity>? = albumsDao.getAlbumsByTitle(title)
        return albums?.let { mapAlbumDbEntityListToMediaList(it) }
    }

    override suspend fun getAlbumDetailsById(id: Int): List<Media>? {
        val albumDetails: List<Media>?
        val album = albumsDao.getAlbumById(id)
        albumDetails = if(album != null){
            val tracks = tracksDao.getTracksByAlbumId(id)
            if(tracks != null) {
                mapAlbumDbEntityListToMediaList(listOf(album))
                        .plus(mapTrackDbEntityListToMediaList(tracks))
            } else {
                mapAlbumDbEntityListToMediaList(listOf(album))
            }
        } else return null
        return albumDetails
    }
}