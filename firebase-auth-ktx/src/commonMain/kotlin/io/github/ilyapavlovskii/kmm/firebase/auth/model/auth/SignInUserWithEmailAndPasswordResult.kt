package io.github.ilyapavlovskii.kmm.firebase.auth.model.auth

import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException

sealed class SignInUserWithEmailAndPasswordResult {
    data class Success(
        val authResult: AuthResult,
    ) : SignInUserWithEmailAndPasswordResult()

    sealed class Error<E : Throwable> : SignInUserWithEmailAndPasswordResult() {
        abstract val exception: E

        data class NotExists(
            override val exception: FirebaseAuthInvalidUserException,
        ) : Error<FirebaseAuthInvalidUserException>()

        data class WrongPassword(
            override val exception: FirebaseAuthInvalidCredentialsException,
        ) : Error<FirebaseAuthInvalidCredentialsException>()

        data class Other(
            override val exception: Throwable,
        ) : Error<Throwable>()
    }
}