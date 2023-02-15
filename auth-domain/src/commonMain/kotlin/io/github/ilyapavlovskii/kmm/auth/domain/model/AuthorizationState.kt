package io.github.ilyapavlovskii.kmm.auth.domain.model

sealed class AuthorizationState {
    object NotAuthorized : AuthorizationState()
    object Authorized : AuthorizationState()
}