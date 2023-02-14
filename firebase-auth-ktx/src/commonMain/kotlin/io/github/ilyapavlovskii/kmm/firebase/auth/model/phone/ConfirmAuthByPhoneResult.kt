package io.github.ilyapavlovskii.kmm.firebase.auth.model.phone

import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException

sealed class ConfirmAuthByPhoneResult {
    data class Success(
        val authResult: AuthResult,
    ) : ConfirmAuthByPhoneResult()

    sealed class Error<E: Throwable> : ConfirmAuthByPhoneResult() {
        abstract val exception: E

        /**
         * if the user account you are trying to sign in to has been disabled.
         * Also thrown if credential is an EmailAuthCredential with an email address that does
         * not correspond to an existing user.
         * */
        data class UserNotFound(
            override val exception: FirebaseAuthInvalidUserException,
        ) : Error<FirebaseAuthInvalidUserException>()

        /**
         * If the password is wrong.
         * Thrown if the credential is malformed or has expired.
         * If credential instanceof EmailAuthCredential it will be thrown if
         * the password is incorrect.
         * */
        data class InvalidCredentials(
            override val exception: FirebaseAuthInvalidCredentialsException,
        ) : Error<FirebaseAuthInvalidCredentialsException>()

        data class Other(
            override val exception: Throwable,
        ) : Error<Throwable>()
    }
}
