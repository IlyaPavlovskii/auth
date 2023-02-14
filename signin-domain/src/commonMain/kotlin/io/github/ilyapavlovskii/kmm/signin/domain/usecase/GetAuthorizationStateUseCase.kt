package io.github.ilyapavlovskii.kmm.signin.domain.usecase

import io.github.ilyapavlovskii.kmm.signin.domain.model.AuthorizationState

interface GetAuthorizationStateUseCase {
    fun execute(): AuthorizationState
}