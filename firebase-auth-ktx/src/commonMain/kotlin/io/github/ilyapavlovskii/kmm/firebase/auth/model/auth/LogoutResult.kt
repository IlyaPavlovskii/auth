package io.github.ilyapavlovskii.kmm.firebase.auth.model.auth

sealed class LogoutResult {
    object Success : LogoutResult()
    data class Error(
        val exception: Throwable,
    ) : LogoutResult()
}