package io.github.ilyapavlovskii.kmm.signin.domain.mvi.email

import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailRedux.Effect
import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailRedux.Message
import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailRedux.State
import net.humans.kmm.mvi.ComplexReducer
import net.humans.kmm.mvi.Return
import net.humans.kmm.mvi.pure
import net.humans.kmm.mvi.withEffect

class AuthorizationEmailReducer : ComplexReducer<State, Message, Effect> {
    override fun invoke(state: State, msg: Message): Return<State, Effect> = when (msg) {
        Message.Authorize ->
            state.copy(
                action = State.Action.Processing,
            ).withEffect(
                Effect.SendAuthorizationRequest(
                    email = state.typedEmailValue,
                    password = state.typedPasswordValue,
                )
            )

        Message.ActionHandled -> state.copy(action = null).pure()
        Message.ErrorProcessed -> state.copy(error = null).pure()
        is Message.UpdateTypedEmailValue -> state.copy(
            typedEmailValue = msg.typedEmailValue,
            error = null,
        ).pure()

        Message.SendRestorePassword -> state.withEffect(
            Effect.ResetPassword(email = state.typedEmailValue)
        )

        is Message.UpdateTypedPasswordValue -> state.copy(
            typedPasswordValue = msg.typedPasswordValue,
            error = null,
        ).pure()

        is Message.AuthorizationSuccess -> state.copy(
            action = State.Action.SendConfirmationCodeComplete(
                email = msg.email,
            ),
            error = null,
        ).pure()

        is Message.InvalidEmailHandled -> state.copy(
            action = null,
            error = State.Error.InvalidEmail,
        ).pure()
        Message.AuthorizationFailed -> state.copy(
            action = null,
            error = State.Error.AuthorizationFailed,
        ).pure()

        is Message.InvalidPasswordHandled -> state.copy(
            action = null,
            error = State.Error.InvalidPasswordHandled,
        ).pure()

        Message.ResetPasswordFailed -> state.copy(
            action = null,
            error = State.Error.ResetPasswordFailed
        ).pure()

        is Message.ResetPasswordSuccess -> state.copy(
            action = State.Action.ResetPasswordSuccess(msg.email),
            error = null,
        ).pure()

        Message.UserNotRegistered -> state.copy(
            error = State.Error.UserNotRegistered,
            action = null,
        ).pure()

        is Message.UpdateTermsOfConditionValue -> state.copy(
            termsOfConditionChecked = msg.termsOfConditionChecked,
        ).pure()

        is Message.UpdatePasswordVisibilityValue -> state.copy(
            passwordVisible = msg.visible,
        ).pure()

        Message.WrongPasswordHandled -> state.copy(
            action = State.Action.WrongPasswordHandled,
        ).pure()

        Message.SelectTermsOfConditions -> state.copy(
            action = State.Action.NavigateToTermsOfConditions,
            error = null,
        ).pure()
    }
}