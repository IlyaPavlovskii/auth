package io.github.ilyapavlovskii.kmm.signin.domain.usecase.firebase

import io.github.ilyapavlovskii.kmm.signin.domain.model.AuthorizationState
import io.github.ilyapavlovskii.kmm.signin.domain.usecase.GetAuthorizationStateUseCase
import dev.gitlive.firebase.auth.FirebaseAuth

internal class FirebaseGetAuthorizationStateUseCase(
    private val firebaseAuth: FirebaseAuth,
) : GetAuthorizationStateUseCase {
    override fun execute(): AuthorizationState = if (firebaseAuth.currentUser == null) {
        AuthorizationState.NotAuthorized
    } else {
        AuthorizationState.Authorized
    }
}