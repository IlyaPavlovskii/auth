package io.github.ilyapavlovskii.kmm.signin.domain.mvi.phone

import io.github.ilyapavlovskii.kmm.common.domain.model.Phone
import io.github.ilyapavlovskii.kmm.common.domain.model.SMSCode
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.AuthByPhonePlatformWrapper
import kotlin.time.Duration

object AuthorizationPhoneRedux {
    sealed class State {

        data class InputPhone(
            val typedPhoneValue: String? = null,
            val termsOfConditionChecked: Boolean,
            val processing: Boolean,
            val action: Action? = null,
            val error: Error? = null,
        ) : State() {

            fun isContinuedAvailable(): Boolean = !processing &&
                !typedPhoneValue.isNullOrBlank() &&
                termsOfConditionChecked

            sealed class Action

            sealed class Error {
                object InvalidPhoneNumber : Error()
                object FailedToSendSms : Error()
            }
        }

        data class InputSMSCode(
            val phone: Phone,
            val typedSMSCode: String? = null,
            val processing: Boolean,
            val smsCodeMaxLength: UInt,
            val resendCodeTimer: Duration,
            val action: Action? = null,
            val error: Error? = null
        ) : State() {

            fun isResendActionAvailable(): Boolean = resendCodeTimer <= Duration.ZERO

            sealed class Action {
                object AuthorizationSuccess : Action()
                object RestartResendCodeTimer : Action()
                data class SMSCodeCallback(
                    val smsCode: SMSCode,
                ) : Action()
            }

            sealed class Error {
                object InvalidSMSCodeFormat : Error()
                object ResendConfirmationCodeError : Error()
                object Failed : Error()
            }
        }
    }

    sealed class Message {

        data class UpdateTypedPhoneNumber(
            val phoneNumber: String,
        ) : Message()

        data class UpdateTypedSMSCode(
            val smsCode: String,
        ) : Message()

        data class SendSMSToPhoneNumber(
            val authByPhoneWrapper: AuthByPhonePlatformWrapper,
        ) : Message()

        data class UpdateTermsOfConditionState(
            val checked: Boolean
        ) : Message()

        object SMSSuccessfullySent : Message()
        object ConfirmSMSCode : Message()

        object ActionHandled : Message()

        object ErrorHandled : Message()
        object InvalidSMSCodeFormatHandled : Message()
        object InvalidPhoneNumberHandled : Message()
        object ApiNotAvailableHandled : Message()
        object AuthExceptionHandled : Message()
        object OtherError : Message()

        data class AuthByPhonePlatformWrapperSMSCodeCallback(
            val smsCode: SMSCode
        ) : Message()

        data class ResendConfirmationCode(
            val authByPhoneWrapper: AuthByPhonePlatformWrapper
        ) : Message()

        object ResendConfirmationCodeError : Message()
        data class UpdateTimerValue(
            val resendCodeTimer: Duration,
        ) : Message()

        object InputPhoneNumberProcessed : Message()
    }

    sealed class Effect {

        data class SendSMSToPhoneNumber(
            val phoneNumber: String?,
            val authByPhoneWrapper: AuthByPhonePlatformWrapper,
        ) : Effect()

        data class ConfirmSMSCode(
            val smsCode: String?,
        ) : Effect()
    }
}