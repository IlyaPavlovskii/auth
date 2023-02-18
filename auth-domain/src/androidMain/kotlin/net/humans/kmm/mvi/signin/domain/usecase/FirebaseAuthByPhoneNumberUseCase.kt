package net.humans.kmm.mvi.signin.domain.usecase

import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.PhoneAuthProvider
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone.AuthByPhoneNumberUseCase
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.AndroidAuthByPhonePlatformWrapper
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone.AuthByPhoneNumberInput
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone.AuthByPhoneNumberResult
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.AuthByPhoneResult
import io.github.ilyapavlovskii.kmm.firebase.auth.confirmAuthByPhone
import io.github.ilyapavlovskii.kmm.firebase.auth.authByPhone
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.ConfirmAuthByPhoneResult
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.toPhoneVerificationProvider

internal class FirebaseAuthByPhoneNumberUseCase(
    private val phoneAuthProvider: PhoneAuthProvider,
    private val firebaseAuth: FirebaseAuth
) : AuthByPhoneNumberUseCase<AndroidAuthByPhonePlatformWrapper> {
    override suspend fun execute(
        input: AuthByPhoneNumberInput<AndroidAuthByPhonePlatformWrapper>
    ): AuthByPhoneNumberResult {
        val result = phoneAuthProvider.authByPhone(
            phone = input.phone,
            phoneVerificationProvider = input.authByPhoneWrapper.toPhoneVerificationProvider()
        )
        return when (result) {
            is AuthByPhoneResult.Error.ApiNotAvailable ->
                AuthByPhoneNumberResult.Error.ApiNotAvailable

            is AuthByPhoneResult.Error.AuthException ->
                AuthByPhoneNumberResult.Error.AuthException

            is AuthByPhoneResult.Error.InvalidPhoneNumber ->
                AuthByPhoneNumberResult.Error.InvalidPhoneNumber

            is AuthByPhoneResult.Error.Other ->
                AuthByPhoneNumberResult.Error.Failed

            is AuthByPhoneResult.Error.TooManyRequests ->
                AuthByPhoneNumberResult.Error.ApiNotAvailable

            is AuthByPhoneResult.Success ->
                when (firebaseAuth.confirmAuthByPhone(result.authCredentials)) {
                    is ConfirmAuthByPhoneResult.Error.InvalidCredentials ->
                        AuthByPhoneNumberResult.Error.AuthException

                    is ConfirmAuthByPhoneResult.Error.Other ->
                        AuthByPhoneNumberResult.Error.Failed

                    is ConfirmAuthByPhoneResult.Error.UserNotFound ->
                        AuthByPhoneNumberResult.Error.AuthException

                    is ConfirmAuthByPhoneResult.Success ->
                        AuthByPhoneNumberResult.Success
                }
        }
    }
}