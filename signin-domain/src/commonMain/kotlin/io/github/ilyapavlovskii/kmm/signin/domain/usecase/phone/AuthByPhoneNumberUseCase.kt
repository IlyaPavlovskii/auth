package io.github.ilyapavlovskii.kmm.signin.domain.usecase.phone

import io.github.ilyapavlovskii.kmm.common.domain.model.Phone
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.AuthByPhonePlatformWrapper

interface AuthByPhoneNumberUseCase<T : AuthByPhonePlatformWrapper> {
    suspend fun execute(
        input: AuthByPhoneNumberInput<T>
    ): AuthByPhoneNumberResult
}

data class AuthByPhoneNumberInput<T : AuthByPhonePlatformWrapper>(
    val phone: Phone,
    val authByPhoneWrapper: T,
)

sealed class AuthByPhoneNumberResult {
    object Success : AuthByPhoneNumberResult()

    sealed class Error : AuthByPhoneNumberResult() {
        object AuthException : Error()
        object InvalidPhoneNumber : Error()
        object ApiNotAvailable : Error()
        object Failed : Error()
    }
}
