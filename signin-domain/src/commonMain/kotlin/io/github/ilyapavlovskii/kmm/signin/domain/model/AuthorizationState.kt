package io.github.ilyapavlovskii.kmm.signin.domain.model

sealed class AuthorizationState {
    object NotAuthorized : AuthorizationState()
    object Authorized : AuthorizationState()
}