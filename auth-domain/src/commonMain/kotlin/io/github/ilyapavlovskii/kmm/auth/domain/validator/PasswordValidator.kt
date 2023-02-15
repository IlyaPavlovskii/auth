package io.github.ilyapavlovskii.kmm.auth.domain.validator

import com.chrynan.validator.Invalid
import com.chrynan.validator.Valid
import com.chrynan.validator.ValidationError
import com.chrynan.validator.ValidationResult
import com.chrynan.validator.Validator

internal class PasswordValidator : Validator<String?, String> {
    override fun validate(input: String?): ValidationResult<String> {
        return when {
            input == null -> Invalid(PasswordValidationError.InputIsNull)
            !input.matches(PASSWORD_PATTERN) -> Invalid(PasswordValidationError.InvalidFormat)
            else -> Valid(input)
        }
    }

    companion object {
        private val PASSWORD_PATTERN = "((\\w|\\d){8,})".toRegex()
    }
}

internal sealed class PasswordValidationError(
    override val details: String? = null
) : ValidationError {
    /**
     * The provided input value to the [PasswordValidator] was null.
     * A null value is not a valid password.
     */
    object InputIsNull : PasswordValidationError(
        details = "Input is not a valid Password because it is null."
    )

    /**
     * The provided input value to the [PasswordValidator] was not in a valid password.
     */
    object InvalidFormat : PasswordValidationError(
        details = "Input is not in a valid Password Format."
    )
}