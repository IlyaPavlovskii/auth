package io.github.ilyapavlovskii.kmm.signin.domain.usecase.email

import io.github.ilyapavlovskii.kmm.common.domain.model.Email

interface ResetPasswordByEmailUseCase {
    suspend fun execute(
        input: ResetPasswordByEmailInput,
    ): ResetPasswordByEmailResult
}

data class ResetPasswordByEmailInput(
    val email: Email,
)

sealed class ResetPasswordByEmailResult {
    object Success : ResetPasswordByEmailResult()

    sealed class Error : ResetPasswordByEmailResult() {
        object InvalidUser : Error()
        object Failed : Error()
    }
}
