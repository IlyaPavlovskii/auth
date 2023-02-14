package io.github.ilyapavlovskii.kmm.firebase.auth.model.phone

import dev.gitlive.firebase.FirebaseApiNotAvailableException
import dev.gitlive.firebase.FirebaseTooManyRequestsException
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException

sealed class AuthByPhoneResult {
    data class Success(
        val authCredentials: AuthCredential,
    ) : AuthByPhoneResult()

    sealed class Error<E : Throwable> : AuthByPhoneResult() {
        abstract val exception: E

        /**
         * if the request is in some way malformed (such as an invalid phone number).
         * Check the error message for details.
         * */
        data class InvalidPhoneNumber(
            override val exception: FirebaseAuthInvalidCredentialsException,
        ) : Error<FirebaseAuthInvalidCredentialsException>()

        /**
         * if the sms quota for the project has been exceeded.
         * */
        data class TooManyRequests(
            override val exception: FirebaseTooManyRequestsException,
        ) : Error<FirebaseTooManyRequestsException>()

        /**
         * if this api is called on a device the does not have Google Play Services
         * */
        data class ApiNotAvailable(
            override val exception: FirebaseApiNotAvailableException,
        ) : Error<FirebaseApiNotAvailableException>()

        /**
         * If the app is not authorized to use Firebase Authentication.
         * Verify the app's package name and SHA-1 in the Firebase Console.
         * */
        data class AuthException(
            override val exception: FirebaseAuthException,
        ) : Error<FirebaseAuthException>()

        data class Other(
            override val exception: Throwable,
        ) : Error<Throwable>()
    }
}