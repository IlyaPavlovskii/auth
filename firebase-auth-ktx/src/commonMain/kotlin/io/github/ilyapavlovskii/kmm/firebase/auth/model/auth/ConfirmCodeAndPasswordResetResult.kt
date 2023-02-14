package io.github.ilyapavlovskii.kmm.firebase.auth.model.auth

import dev.gitlive.firebase.auth.FirebaseAuthActionCodeException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException

sealed class ConfirmCodeAndPasswordResetResult {
    object Success : ConfirmCodeAndPasswordResetResult()
    sealed class Error<E : Throwable> : ConfirmCodeAndPasswordResetResult() {
        abstract val exception: E

        data class IllegalCodeOrPassword(
            override val exception: IllegalArgumentException,
        ) : Error<IllegalArgumentException>()

        data class CodeExpired(
            override val exception: FirebaseAuthActionCodeException,
        ) : Error<FirebaseAuthActionCodeException>()

        data class InvalidCode(
            override val exception: FirebaseAuthInvalidUserException,
        ) : Error<FirebaseAuthInvalidUserException>()

        data class Other(
            override val exception: Throwable,
        ) : Error<Throwable>()
    }
}