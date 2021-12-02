package com.lenatopoleva.musicsearch.model.data

sealed class RegistrationState {
    data class Success(val doneSuccessful: Boolean): RegistrationState()
    data class Error(val error: Throwable): RegistrationState()
}