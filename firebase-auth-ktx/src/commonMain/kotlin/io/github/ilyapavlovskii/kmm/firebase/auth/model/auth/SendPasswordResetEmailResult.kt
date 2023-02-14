package io.github.ilyapavlovskii.kmm.firebase.auth.model.auth

import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException

sealed class SendPasswordResetEmailResult {
    object Success : SendPasswordResetEmailResult()
    sealed class Error<E : Throwable> : SendPasswordResetEmailResult() {
        abstract val exception: E

        data class InvalidUser(
            override val exception: FirebaseAuthInvalidUserException,
        ) : Error<FirebaseAuthInvalidUserException>()

        data class Other(
            override val exception: Throwable,
        ) : Error<Throwable>()
    }
}