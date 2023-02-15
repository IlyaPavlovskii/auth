package io.github.ilyapavlovskii.kmm.auth.domain.mvi.email

import io.github.ilyapavlovskii.kmm.common.domain.model.Email

object AuthorizationEmailRedux {
    data class State(
        val typedEmailValue: String? = null,
        val typedPasswordValue: String? = null,
        val termsOfConditionChecked: Boolean = false,
        val passwordVisible: Boolean = false,
        val error: Error? = null,
        val action: Action? = null,
    ) {

        fun isContinueActionAvailable(): Boolean = !typedEmailValue.isNullOrBlank() &&
            !typedPasswordValue.isNullOrBlank() &&
            termsOfConditionChecked &&
            error == null

        sealed class Error {
            object InvalidEmail : Error()
            object AuthorizationFailed : Error()
            object InvalidPasswordHandled : Error()
            object ResetPasswordFailed : Error()
            object UserNotRegistered : Error()
        }

        sealed class Action {

            object Processing : Action()

            object NavigateToTermsOfConditions : Action()

            data class SendConfirmationCodeComplete(
                val email: Email,
            ) : Action()

            data class ResetPasswordSuccess(
                val email: Email,
            ) : Action()

            object WrongPasswordHandled : Action()
        }
    }

    sealed class Message {
        object Authorize : Message()

        data class UpdateTypedEmailValue(
            val typedEmailValue: String,
        ) : Message()

        data class UpdateTypedPasswordValue(
            val typedPasswordValue: String,
        ) : Message()

        data class UpdateTermsOfConditionValue(
            val termsOfConditionChecked: Boolean
        ) : Message()

        object SelectTermsOfConditions : Message()

        data class UpdatePasswordVisibilityValue(
            val visible: Boolean,
        ) : Message()

        data class AuthorizationSuccess(
            val email: Email,
        ) : Message()

        data class InvalidEmailHandled(
            val email: String?,
        ) : Message()

        data class InvalidPasswordHandled(
            val password: String?,
        ) : Message()

        object WrongPasswordHandled : Message()

        object AuthorizationFailed : Message()

        object UserNotRegistered : Message()

        object SendRestorePassword : Message()
        object ResetPasswordFailed : Message()
        data class ResetPasswordSuccess(
            val email: Email,
        ) : Message()

        object ActionHandled : Message()
        object ErrorHandled : Message()
    }

    sealed class Effect {
        data class SendAuthorizationRequest(
            val email: String?,
            val password: String?,
        ) : Effect()

        data class ResetPassword(
            val email: String?,
        ) : Effect()
    }
}