package io.github.ilyapavlovskii.kmm.firebase.auth.model.auth

import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException

sealed class SignInWithEmailLinkResult {
    data class Success(
        val authResult: AuthResult,
    ) : SignInWithEmailLinkResult()
    sealed class Error<E: Throwable> : SignInWithEmailLinkResult(){
        abstract val exception: E
        /**
         * If the user account corresponding to email does not exist or has been disabled
         * */
        data class InvalidUser(
            override val exception: FirebaseAuthInvalidUserException,
        ) : Error<FirebaseAuthInvalidUserException>()

        /**
         * If the password is wrong
         * */
        data class InvalidCredentials(
            override val exception: FirebaseAuthInvalidCredentialsException,
        ) : Error<FirebaseAuthInvalidCredentialsException>()

        data class Other(
            override val exception: Throwable,
        ): Error<Throwable>()
    }
}