package io.github.ilyapavlovskii.kmm.signin.presentation.email

import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailRedux.State
import io.github.ilyapavlovskii.kmm.signin.presentation.R
import net.humans.kmm.mvi.ViewStateConverter

internal class SignInEmailViewStateConverter : ViewStateConverter<State, SignInEmailViewState> {
    override fun convert(state: State): SignInEmailViewState {
        return state.action?.let { safeAction ->
            val viewState = state.toAuthByEmailScreenViewStatePure()
            when (safeAction) {
                State.Action.Processing -> viewState.copy(
                    processing = true,
                )

                is State.Action.SendConfirmationCodeComplete ->
                    viewState.copy(
                        email = safeAction.email.value,
                        action = SignInEmailViewState.Action.AuthSuccess,
                        error = null,
                    )

                is State.Action.ResetPasswordSuccess ->
                    viewState.copy(
                        action = SignInEmailViewState.Action.Dialog(
                            messageRes = R.string.signin__sign_by_email_reset_message_pattern,
                            arguments = listOf(safeAction.email.value),
                        ),
                        error = null,
                    )

                State.Action.WrongPasswordHandled ->
                    viewState.copy(
                        action = null,
                        error = SignInEmailViewState.Error.Password(
                            messageRes = R.string.signin__sign_by_email_wrong_password,
                            wrongPassword = true,
                        ),
                    )

                State.Action.NavigateToTermsOfConditions ->
                    viewState.copy(
                        action = SignInEmailViewState.Action.NavigateToTermsOfConditions,
                        error = null,
                    )
            }
        } ?: state.error?.let { safeError ->
            val viewState = state.toAuthByEmailScreenViewStatePure()
            when (safeError) {
                is State.Error.InvalidEmail -> viewState.copy(
                    action = null,
                    error = SignInEmailViewState.Error.Email(
                        messageRes = R.string.signin__sign_by_email_error_message
                    ),
                )

                State.Error.AuthorizationFailed -> viewState.copy(
                    action = SignInEmailViewState.Action.Dialog(
                        messageRes = R.string.signin__sign_by_email_auth_failed,
                        arguments = emptyList(),
                    ),
                    error = null,
                )

                State.Error.InvalidPasswordHandled -> viewState.copy(
                    action = null,
                    error = SignInEmailViewState.Error.Password(
                        messageRes = R.string.signin__sign_by_email_invalid_password,
                        wrongPassword = false,
                    ),
                )

                State.Error.ResetPasswordFailed -> viewState.copy(
                    action = SignInEmailViewState.Action.Dialog(
                        messageRes = R.string.signin__sign_by_email_reset_password_failed,
                        arguments = emptyList(),
                    ),
                    error = null,
                )

                State.Error.UserNotRegistered -> viewState.copy(
                    action = SignInEmailViewState.Action.Dialog(
                        messageRes = R.string.signin__sign_by_email_user_not_found,
                        arguments = emptyList(),
                    ),
                    error = null,
                )
            }
        } ?: state.toAuthByEmailScreenViewStatePure()
    }

    private fun State.toAuthByEmailScreenViewStatePure():
        SignInEmailViewState = SignInEmailViewState(
        email = this.typedEmailValue.orEmpty(),
        password = this.typedPasswordValue.orEmpty(),
        termsOfConditionChecked = this.termsOfConditionChecked,
        isContinueActionAvailable = this.isContinueActionAvailable(),
        passwordVisible = this.passwordVisible,
        processing = false,
        action = null,
        error = null,
    )
}
