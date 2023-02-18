package io.github.ilyapavlovskii.kmm.auth.presentation.phone

import androidx.annotation.StringRes
import io.github.ilyapavlovskii.kmm.common.domain.model.SMSCode
import kotlin.time.Duration

internal sealed class AuthByPhoneViewState {
    data class InputPhone(
        val phone: String = "",
        val termsOfConditionChecked: Boolean = false,
        val isContinueAvailable: Boolean = false,
        val smsCodeMaxLength: UInt = 0u,
        val isProcessing: Boolean = false,
        val action: Action? = null,
        val error: Error? = null,
    ) : AuthByPhoneViewState() {

        sealed class Action {
            object NavigateToTermsOfConditions : Action()
        }

        sealed class Error {
            data class InvalidPhoneNumber(
                @StringRes val messageRes: Int,
            ) : Error()

            data class FailedToSendSMS(
                @StringRes val messageRes: Int,
            ) : Error()
        }
    }

    data class InputSMS(
        val phone: String,
        val smsCode: String,
        val smsCodeMaxLength: UInt,
        val resendCodeTimer: Duration,
        val resendEnabled: Boolean,
        val processing: Boolean,
        val action: Action? = null,
        val error: Error? = null,
    ) : AuthByPhoneViewState() {

        sealed class Action {
            object AuthorizationSuccess : Action()
            object RestartResendCodeTimer : Action()
            data class SMSCodeCallback(
                val smsCode: SMSCode,
            ) : Action()
        }

        sealed class Error {
            data class Failed(
                @StringRes val messageRes: Int,
            ) : Error()
        }
    }
}