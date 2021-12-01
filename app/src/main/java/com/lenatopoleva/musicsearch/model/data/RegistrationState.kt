package com.lenatopoleva.musicsearch.model.data

sealed class RegistrationState{
    data class Success(val isRegistrated: Boolean) : RegistrationState()
    data class Error(val error: Throwable) : RegistrationState()
}
