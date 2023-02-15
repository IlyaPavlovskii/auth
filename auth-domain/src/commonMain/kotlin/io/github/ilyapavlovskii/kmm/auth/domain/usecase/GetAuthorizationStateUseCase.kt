package io.github.ilyapavlovskii.kmm.auth.domain.usecase

import io.github.ilyapavlovskii.kmm.auth.domain.model.AuthorizationState

interface GetAuthorizationStateUseCase {
    fun execute(): AuthorizationState
}