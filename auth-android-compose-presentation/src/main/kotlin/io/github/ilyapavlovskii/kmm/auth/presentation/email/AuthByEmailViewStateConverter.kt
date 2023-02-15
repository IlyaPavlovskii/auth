package io.github.ilyapavlovskii.kmm.auth.presentation.email

import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.AuthorizationEmailRedux.State
import io.github.ilyapavlovskii.kmm.auth.presentation.R
import net.humans.kmm.mvi.ViewStateConverter

internal class AuthByEmailViewStateConverter : ViewStateConverter<State, AuthByEmailViewState> {
    override fun convert(state: State): AuthByEmailViewState {
        val viewState = state.toAuthByEmailScreenViewStatePure()
        return state.action?.let { safeAction ->
            when (safeAction) {
                State.Action.Processing -> viewState.copy(
                    processing = true,
                )

                is State.Action.SendConfirmationCodeComplete ->
                    viewState.copy(
                        email = safeAction.email.value,
                        action = AuthByEmailViewState.Action.AuthSuccess,
                        error = null,
                    )

                is State.Action.ResetPasswordSuccess ->
                    viewState.copy(
                        action = AuthByEmailViewState.Action.Dialog(
                            messageRes = R.string.auth__auth_by_email_reset_message_pattern,
                            arguments = listOf(safeAction.email.value),
                        ),
                        error = null,
                    )

                State.Action.WrongPasswordHandled ->
                    viewState.copy(
                        action = null,
                        error = AuthByEmailViewState.Error.Password(
                            messageRes = R.string.auth__auth_by_email_wrong_password,
                            wrongPassword = true,
                        ),
                    )

                State.Action.NavigateToTermsOfConditions ->
                    viewState.copy(
                        action = AuthByEmailViewState.Action.NavigateToTermsOfConditions,
                        error = null,
                    )
            }
        } ?: state.error?.let { safeError ->
            when (safeError) {
                is State.Error.InvalidEmail -> viewState.copy(
                    action = null,
                    error = AuthByEmailViewState.Error.Email(
                        messageRes = R.string.auth__auth_by_email_error_message
                    ),
                )

                State.Error.AuthorizationFailed -> viewState.copy(
                    action = AuthByEmailViewState.Action.Dialog(
                        messageRes = R.string.auth__auth_by_email_auth_failed,
                        arguments = emptyList(),
                    ),
                    error = null,
                )

                State.Error.InvalidPasswordHandled -> viewState.copy(
                    action = null,
                    error = AuthByEmailViewState.Error.Password(
                        messageRes = R.string.auth__auth_by_email_invalid_password,
                        wrongPassword = false,
                    ),
                )

                State.Error.ResetPasswordFailed -> viewState.copy(
                    action = AuthByEmailViewState.Action.Dialog(
                        messageRes = R.string.auth__auth_by_email_reset_password_failed,
                        arguments = emptyList(),
                    ),
                    error = null,
                )

                State.Error.UserNotRegistered -> viewState.copy(
                    action = AuthByEmailViewState.Action.Dialog(
                        messageRes = R.string.auth__auth_by_email_user_not_found,
                        arguments = emptyList(),
                    ),
                    error = null,
                )
            }
        } ?: viewState
    }

    private fun State.toAuthByEmailScreenViewStatePure():
        AuthByEmailViewState = AuthByEmailViewState(
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
