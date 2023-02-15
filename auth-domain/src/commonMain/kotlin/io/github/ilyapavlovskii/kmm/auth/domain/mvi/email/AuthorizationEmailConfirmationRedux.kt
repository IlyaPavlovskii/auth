package io.github.ilyapavlovskii.kmm.auth.domain.mvi.email

import io.github.ilyapavlovskii.kmm.common.domain.model.Email
import kotlin.time.Duration

object AuthorizationEmailConfirmationRedux {
    data class State(
        val email: Email,
        val confirmationCodeValue: String? = null,
        val confirmationCodeMaxLength: UInt,
        val resendCodeTimer: Duration,
        val error: Error? = null,
        val action: Action? = null,
    ) {

        fun isResendActionAvailable(): Boolean = resendCodeTimer <= Duration.ZERO

        sealed class Error {
            object InvalidConfirmationCode : Error()
            object ResendConfirmationCodeError : Error()
        }

        sealed class Action {
            object Processing : Action()
            object ConfirmationCodeSuccess : Action()
        }
    }

    sealed class Message {
        data class UpdateConfirmationCode(
            val value: String,
        ) : Message()

        sealed class CheckConfirmationCode : Message() {
            object Success : CheckConfirmationCode()
            object InvalidConfirmationCode : CheckConfirmationCode()
        }

        data class UpdateEmailCodeLength(
            val email: Email,
            val codeLength: UInt,
        ) : Message()

        object ActionHandled : Message()
        object ErrorProcessed : Message()
        object ResendConfirmationCode : Message()
        object ResendConfirmationCodeError : Message()
        data class UpdateTimerValue(
            val resendCodeTimer: Duration,
        ) : Message()
    }

    sealed class Effect {
        data class VerifyConfirmationCode(
            val email: Email,
            val confirmationCode: String,
        ) : Effect()

        data class ResendConfirmationCode(
            val email: Email,
        ) : Effect()
    }
}