package io.github.ilyapavlovskii.kmm.signin.domain.usecase.email

import io.github.ilyapavlovskii.kmm.common.domain.model.Email
import io.github.ilyapavlovskii.kmm.common.domain.model.Password

interface SignByEmailUseCase {
    suspend fun execute(input: SignByEmailInput): SignByEmailResult
}

data class SignByEmailInput(
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
