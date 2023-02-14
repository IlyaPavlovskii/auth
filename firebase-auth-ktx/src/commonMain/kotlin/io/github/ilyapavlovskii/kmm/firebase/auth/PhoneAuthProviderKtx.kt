package io.github.ilyapavlovskii.kmm.firebase.auth

import dev.gitlive.firebase.FirebaseApiNotAvailableException
import dev.gitlive.firebase.FirebaseTooManyRequestsException
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.PhoneAuthProvider
import dev.gitlive.firebase.auth.PhoneVerificationProvider
import io.github.ilyapavlovskii.kmm.common.domain.model.Phone
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.AuthByPhoneResult
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.ConfirmAuthByPhoneResult

suspend fun PhoneAuthProvider.authByPhone(
    phone: Phone,
    phoneVerificationProvider: PhoneVerificationProvider,
): AuthByPhoneResult = try {
    val result = this.verifyPhoneNumber(
        phoneNumber = phone.toE164().value,
        verificationProvider = phoneVerificationProvider
    )
    AuthByPhoneResult.Success(result)
} catch (faice: FirebaseAuthInvalidCredentialsException) {
    AuthByPhoneResult.Error.InvalidPhoneNumber(faice)
} catch (ftmre: FirebaseTooManyRequestsException) {
    AuthByPhoneResult.Error.TooManyRequests(ftmre)
} catch (fanae: FirebaseApiNotAvailableException) {
    AuthByPhoneResult.Error.ApiNotAvailable(fanae)
} catch (fae: FirebaseAuthException) {
    AuthByPhoneResult.Error.AuthException(fae)
} catch (ex: Throwable) {
    AuthByPhoneResult.Error.Other(ex)
}

suspend fun FirebaseAuth.confirmAuthByPhone(
    authCredential: AuthCredential,
): ConfirmAuthByPhoneResult = try {
    ConfirmAuthByPhoneResult.Success(this.signInWithCredential(authCredential))
} catch (faiue: FirebaseAuthInvalidUserException) {
    ConfirmAuthByPhoneResult.Error.UserNotFound(faiue)
} catch (faice: FirebaseAuthInvalidCredentialsException) {
    ConfirmAuthByPhoneResult.Error.InvalidCredentials(faice)
} catch (ex: Throwable) {
    ConfirmAuthByPhoneResult.Error.Other(ex)
}