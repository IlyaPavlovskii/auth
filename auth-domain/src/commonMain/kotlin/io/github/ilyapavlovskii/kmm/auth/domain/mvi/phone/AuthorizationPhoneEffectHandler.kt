package io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone

import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux.Effect
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux.Message
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone.AuthByPhoneNumberInput
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone.AuthByPhoneNumberUseCase
import com.chrynan.validator.ValidationResult
import com.chrynan.validator.Validator
import io.github.ilyapavlovskii.kmm.common.domain.model.Phone
import io.github.ilyapavlovskii.kmm.common.domain.model.SMSCode
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.AuthByPhonePlatformWrapper
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone.AuthByPhoneNumberResult
import net.humans.kmm.mvi.CoroutineEffectHandler

internal class AuthorizationPhoneEffectHandler<T : AuthByPhonePlatformWrapper>(
    private val authByPhoneNumberUseCase: AuthByPhoneNumberUseCase<T>,
    private val smsCodeValidator: Validator<String?, SMSCode>,
    private val phoneNumberValidator: Validator<String?, Phone>,
) : CoroutineEffectHandler<Effect, Message> {

    override suspend fun handle(eff: Effect): Message? = when (eff) {
        is Effect.ConfirmSMSCode -> validateSMSCode(eff.smsCode)
        is Effect.SendSMSToPhoneNumber -> sendSMSToPhoneNumber(eff)
    }

    private suspend fun sendSMSToPhoneNumber(eff: Effect.SendSMSToPhoneNumber): Message? {
        val phone: Phone = when (val result = phoneNumberValidator.validate(eff.phoneNumber)) {
            is ValidationResult.Invalid -> return Message.InvalidPhoneNumberHandled
            is ValidationResult.Valid -> result.value
        }
        val input = AuthByPhoneNumberInput(
            phone = phone,
            authByPhoneWrapper = eff.authByPhoneWrapper as T
        )
        return when (authByPhoneNumberUseCase.execute(input = input)) {
            AuthByPhoneNumberResult.Error.ApiNotAvailable -> Message.ApiNotAvailableHandled
            AuthByPhoneNumberResult.Error.AuthException -> Message.AuthExceptionHandled
            AuthByPhoneNumberResult.Error.Failed -> Message.OtherError
            AuthByPhoneNumberResult.Error.InvalidPhoneNumber -> Message.InvalidPhoneNumberHandled
            AuthByPhoneNumberResult.Success -> Message.InputPhoneNumberProcessed
        }
    }

    private fun validateSMSCode(smsCode: String?): Message =
        when (val result = smsCodeValidator.validate(smsCode)) {
            is ValidationResult.Invalid -> Message.InvalidSMSCodeFormatHandled
            is ValidationResult.Valid ->
                Message.AuthByPhonePlatformWrapperSMSCodeCallback(result.value)
        }
}