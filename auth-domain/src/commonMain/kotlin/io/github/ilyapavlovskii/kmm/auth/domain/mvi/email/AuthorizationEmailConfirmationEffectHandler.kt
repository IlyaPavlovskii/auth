package io.github.ilyapavlovskii.kmm.auth.domain.mvi.email

import io.github.ilyapavlovskii.kmm.auth.domain.model.ConfirmationCode
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.AuthorizationEmailConfirmationRedux.Effect
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.AuthorizationEmailConfirmationRedux.Message
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.SendEmailRequestInput
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.SendEmailRequestResult
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.SendEmailRequestUseCase
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.ValidateEmailRequestCodeInput
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.ValidateEmailRequestCodeResult
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.ValidateEmailRequestCodeUseCase
import net.humans.kmm.mvi.CoroutineEffectHandler

class AuthorizationEmailConfirmationEffectHandler(
    private val sendEmailRequestUseCase: SendEmailRequestUseCase,
    private val validateEmailRequestCodeUseCase: ValidateEmailRequestCodeUseCase,
) : CoroutineEffectHandler<Effect, Message> {
    override suspend fun handle(eff: Effect): Message {
        return when (eff) {
            is Effect.VerifyConfirmationCode -> verifyConfirmationCode(eff)
            is Effect.ResendConfirmationCode -> resendConfirmation(eff)
        }
    }

    private suspend fun resendConfirmation(eff: Effect.ResendConfirmationCode): Message {
        val input = SendEmailRequestInput(eff.email.value)
        return when(val result = sendEmailRequestUseCase.execute(input)) {
            SendEmailRequestResult.SendEmailRequestError.InvalidEmail ->
                Message.ResendConfirmationCodeError
            is SendEmailRequestResult.SendEmailRequestSuccess ->
                Message.UpdateEmailCodeLength(
                email = result.email,
                codeLength = result.codeLength,
            )
        }
    }

    private suspend fun verifyConfirmationCode(eff: Effect.VerifyConfirmationCode): Message {
        val input = ValidateEmailRequestCodeInput(
            email = eff.email,
            code = ConfirmationCode.createOrNull(eff.confirmationCode)
                ?: return Message.CheckConfirmationCode.InvalidConfirmationCode
        )
        return when (validateEmailRequestCodeUseCase.execute(input = input)) {
            ValidateEmailRequestCodeResult.ValidateEmailRequestCodeError.Failed ->
                Message.CheckConfirmationCode.InvalidConfirmationCode
            ValidateEmailRequestCodeResult.ValidateEmailRequestCodeSuccess ->
                Message.CheckConfirmationCode.Success
        }
    }
}