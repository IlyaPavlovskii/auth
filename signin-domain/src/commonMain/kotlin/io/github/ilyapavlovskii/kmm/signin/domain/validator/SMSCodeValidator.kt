package io.github.ilyapavlovskii.kmm.signin.domain.validator

import com.chrynan.validator.Invalid
import com.chrynan.validator.Valid
import com.chrynan.validator.ValidationError
import com.chrynan.validator.ValidationResult
import com.chrynan.validator.Validator
import io.github.ilyapavlovskii.kmm.common.domain.model.SMSCode

internal abstract class SMSCodeValidator(
    private val smsCodePattern: Regex,
) : Validator<String?, SMSCode> {
    override fun validate(input: String?): ValidationResult<SMSCode> = when {
        input == null -> Invalid(SMSCodeValidationError.InputIsNull)
        !input.matches(smsCodePattern) -> Invalid(SMSCodeValidationError.InvalidFormat)
        else -> try {
            Valid(SMSCode(input))
        } catch (ise: IllegalStateException) {
            Invalid(SMSCodeValidationError.InvalidFormat)
        }
    }
}

internal sealed class SMSCodeValidationError(
    override val details: String? = null
) : ValidationError {
    /**
     * The provided input value to the [SMSCodeValidator] was null.
     * A null value is not a valid password.
     */
    object InputIsNull : SMSCodeValidationError(
        details = "Input is not a valid SMS code because it is null."
    )

    /**
     * The provided input value to the [SMSCodeValidator] was not in a valid SMS code.
     */
    object InvalidFormat : SMSCodeValidationError(
        details = "Input is not in a valid SMS code Format."
    )
}

internal class FirebaseSMSCodeValidator : SMSCodeValidator(
    smsCodePattern = "\\d{6}".toRegex()
)