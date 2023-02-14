package io.github.ilyapavlovskii.kmm.firebase.auth.model.auth

import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException

sealed class CreateUserWithEmailAndPasswordResult {
    data class Success(
        val authResult: AuthResult,
    ) : CreateUserWithEmailAndPasswordResult()

    sealed class Error<E : Throwable> : CreateUserWithEmailAndPasswordResult() {
        abstract val exception: E

        data class AlreadyExists(
            override val exception: FirebaseAuthUserCollisionException,
        ) : Error<FirebaseAuthUserCollisionException>()

        data class InvalidEmail(
            override val exception: FirebaseAuthInvalidCredentialsException,
        ) : Error<FirebaseAuthInvalidCredentialsException>()

        data class Other(
            override val exception: Throwable,
        ) : Error<Throwable>()
    }
}