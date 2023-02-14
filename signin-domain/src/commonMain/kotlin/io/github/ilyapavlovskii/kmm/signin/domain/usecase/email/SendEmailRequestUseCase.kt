package io.github.ilyapavlovskii.kmm.signin.domain.usecase.email

import io.github.ilyapavlovskii.kmm.common.domain.model.Email

interface SendEmailRequestUseCase {
    suspend fun execute(input: SendEmailRequestInput): SendEmailRequestResult
}

data class SendEmailRequestInput(
    val email: String,
)

sealed class SendEmailRequestResult {
    sealed class SendEmailRequestError : SendEmailRequestResult() {
        object InvalidEmail : SendEmailRequestError()
    }

    data class SendEmailRequestSuccess(
        val email: Email,
        val codeLength: UInt,
    ) : SendEmailRequestResult()
}
