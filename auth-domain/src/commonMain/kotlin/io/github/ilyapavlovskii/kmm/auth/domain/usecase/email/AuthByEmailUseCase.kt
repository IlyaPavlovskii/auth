package io.github.ilyapavlovskii.kmm.auth.domain.usecase.email

import io.github.ilyapavlovskii.kmm.common.domain.model.Email
import io.github.ilyapavlovskii.kmm.common.domain.model.Password

interface AuthByEmailUseCase {
    suspend fun execute(input: AuthByEmailInput): SignByEmailResult
}

data class AuthByEmailInput(
    val email: Email,
    val password: Password,
)

sealed class SignByEmailResult {
    data class Success(
        val email: Email,
    ) : SignByEmailResult()

    sealed class Error : SignByEmailResult() {
        object InvalidEmail : Error()
        object WrongPassword : Error()
        object Failed : Error()
    }
}
