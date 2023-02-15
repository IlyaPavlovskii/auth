package io.github.ilyapavlovskii.kmm.auth.domain.validator

import com.chrynan.validator.Invalid
import com.chrynan.validator.Valid
import com.chrynan.validator.ValidationError
import com.chrynan.validator.ValidationResult
import com.chrynan.validator.Validator
import io.github.ilyapavlovskii.kmm.common.domain.model.Phone

internal class PhoneNumberValidator : Validator<String?, Phone> {
    override fun validate(input: String?): ValidationResult<Phone> = when (input) {
        null -> Invalid(PhoneNumberValidationError.InputIsNull)
        else -> try {
            Valid(Phone.E164(input))
        } catch(ise: IllegalStateException) {
            Invalid(PhoneNumberValidationError.InvalidFormat)
        }
    }

}

internal sealed class PhoneNumberValidationError(
    override val details: String? = null
) : ValidationError {
    /**
     * The provided input value to the [PhoneNumberValidator] was null.
     * A null value is not a valid phone number.
     */
    object InputIsNull : PasswordValidationError(
        details = "Input is not a valid Phone because it is null."
    )

    /**
     * The provided input value to the [PhoneNumberValidator] was not in a valid phone.
     */
    object InvalidFormat : PasswordValidationError(
        details = "Input is not in a valid Phone Format."
    )
}