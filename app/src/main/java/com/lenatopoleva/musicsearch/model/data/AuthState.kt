package com.lenatopoleva.musicsearch.model.data

import com.lenatopoleva.musicsearch.model.data.entity.User

sealed class AuthState {
    object Initial : AuthState()
    data class Success(val data: User?) : AuthState()
    data class Error(val error: Throwable) : AuthState()
}

