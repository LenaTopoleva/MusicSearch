package com.lenatopoleva.musicsearch.model.datasource.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "users",
    indices = [Index(value = ["email"], unique = true),
    Index(value = ["auth"], unique = false),
    Index(value = ["email", "password"], unique = true)])

data class UserDbEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val surname: String,
    val birthday: String,
    val phone: String,
    val email: String,
    val password: String,
    val auth: Boolean = false
    )