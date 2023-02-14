package io.github.ilyapavlovskii.kmm.signin.domain.usecase.email

import io.github.ilyapavlovskii.kmm.common.domain.model.Email
import io.github.ilyapavlovskii.kmm.signin.domain.model.ConfirmationCode

interface ValidateEmailRequestCodeUseCase {
    suspend fun execute(
        input: ValidateEmailRequestCodeInput
    ): ValidateEmailRequestCodeResult
}

data class ValidateEmailRequestCodeInput(
    val email: Email,
    val code: ConfirmationCode,
)

sealed class ValidateEmailRequestCodeResult {
    sealed class ValidateEmailRequestCodeError : ValidateEmailRequestCodeResult() {
        object Failed : ValidateEmailRequestCodeError()
    }

    object ValidateEmailRequestCodeSuccess : ValidateEmailRequestCodeResult()
}
