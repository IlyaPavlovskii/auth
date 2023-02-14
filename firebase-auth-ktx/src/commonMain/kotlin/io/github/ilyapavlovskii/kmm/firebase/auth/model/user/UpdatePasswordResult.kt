package io.github.ilyapavlovskii.kmm.firebase.auth.model.user

import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthRecentLoginRequiredException

sealed class UpdatePasswordResult {
    object Success : UpdatePasswordResult()
    sealed class Error<E : Throwable> : UpdatePasswordResult() {
        abstract val exception: E

        /**
         * If the current user's account has been disabled, deleted, or its credentials are no longer valid
         * */
        data class InvalidUser(
            override val exception: FirebaseAuthInvalidUserException,
        ) : Error<FirebaseAuthInvalidUserException>()

        /**
         * If the user's last sign-in time does not meet the security threshold.
         * Use reauthenticate(AuthCredential) to resolve.
         * This does not apply if the user is anonymous.
         * */
        data class AuthorizationRequired(
            override val exception: FirebaseAuthRecentLoginRequiredException
        ) : Error<FirebaseAuthRecentLoginRequiredException>()

        data class Other(
            override val exception: Throwable,
        ) : Error<Throwable>()
    }
}