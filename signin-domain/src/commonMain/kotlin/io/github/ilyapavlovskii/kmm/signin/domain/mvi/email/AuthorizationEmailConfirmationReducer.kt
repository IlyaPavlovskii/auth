package io.github.ilyapavlovskii.kmm.signin.domain.mvi.email

import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailConfirmationRedux.Effect
import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailConfirmationRedux.Message
import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailConfirmationRedux.State
import net.humans.kmm.mvi.ComplexReducer
import net.humans.kmm.mvi.Return
import net.humans.kmm.mvi.pure
import net.humans.kmm.mvi.withEffect

internal class AuthorizationEmailConfirmationReducer : ComplexReducer<State, Message, Effect> {
    override fun invoke(state: State, msg: Message): Return<State, Effect> {
        return when (msg) {
            Message.ActionHandled -> state.copy(action = null).pure()
            Message.CheckConfirmationCode.InvalidConfirmationCode ->
                state.copy(
                    confirmationCodeValue = null,
                    error = State.Error.InvalidConfirmationCode,
                    action = null,
                ).pure()
            Message.CheckConfirmationCode.Success ->
                state.copy(
                    error = null,
                    action = State.Action.ConfirmationCodeSuccess
                ).pure()
            Message.ErrorProcessed -> state.copy(error = null).pure()
            is Message.UpdateConfirmationCode -> if (
                msg.value.length == state.confirmationCodeMaxLength.toInt()
            ) {
                state.copy(
                    action = State.Action.Processing,
                    confirmationCodeValue = msg.value,
                    error = null,
                ).withEffect(Effect.VerifyConfirmationCode(
                    email = state.email,
                    confirmationCode = msg.value,
                ))
            } else {
                state.copy(
                    confirmationCodeValue = msg.value,
                    error = null,
                ).pure()
            }
            Message.ResendConfirmationCode -> state.withEffect(Effect.ResendConfirmationCode(
                email = state.email,
            ))
            Message.ResendConfirmationCodeError -> state.copy(
                error = State.Error.ResendConfirmationCodeError,
            ).pure()
            is Message.UpdateEmailCodeLength -> state.copy(
                email = msg.email,
                confirmationCodeMaxLength = msg.codeLength,
            ).pure()

            is Message.UpdateTimerValue -> state.copy(
                resendCodeTimer = msg.resendCodeTimer,
            ).pure()
        }
    }
}