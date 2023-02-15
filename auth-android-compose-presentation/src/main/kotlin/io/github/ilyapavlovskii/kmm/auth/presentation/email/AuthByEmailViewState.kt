package io.github.ilyapavlovskii.kmm.auth.presentation.email

import androidx.annotation.StringRes

internal data class AuthByEmailViewState(
    val email: String,
    val password: String,
    val passwordVisible: Boolean,
    val termsOfConditionChecked: Boolean,
    val processing: Boolean,
    val isContinueActionAvailable: Boolean,
    val action: Action? = null,
    val error: Error? = null,
) {

    sealed class Action {
        object AuthSuccess : Action()
        object NavigateToTermsOfConditions : Action()
        data class Dialog(
            @StringRes val messageRes: Int,
            val arguments: List<Any>,
        ) : Action()
    }

    sealed class Error {
        data class Email(
            @StringRes val messageRes: Int,
        ) : Error()

        data class Password(
            @StringRes val messageRes: Int,
            val wrongPassword: Boolean,
        ) : Error()
    }

    companion object {
        val DEFAULT = AuthByEmailViewState(
            email = "",
            password = "",
            passwordVisible = false,
            termsOfConditionChecked = false,
            processing = false,
            isContinueActionAvailable = true,
        )
    }
}