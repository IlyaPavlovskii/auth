package io.github.ilyapavlovskii.kmm.auth.presentation.phone

import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux.State
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone.SMSCodeMaxLengthProvider
import io.github.ilyapavlovskii.kmm.auth.presentation.R
import net.humans.kmm.mvi.ViewStateConverter

internal class AuthByPhoneViewStateConverter(
    private val smsCodeMaxLengthProvider: SMSCodeMaxLengthProvider,
) : ViewStateConverter<State, AuthByPhoneViewState> {
    override fun convert(state: State): AuthByPhoneViewState = when (state) {
        is State.InputPhone -> AuthByPhoneViewState.InputPhone(
            phone = state.typedPhoneValue.orEmpty(),
            termsOfConditionChecked = state.termsOfConditionChecked,
            isContinueAvailable = state.isContinuedAvailable(),
            smsCodeMaxLength = smsCodeMaxLengthProvider.getSMSMaxCodeLength(),
            isProcessing = state.processing,
            action = state.action?.toPresentationAction(),
            error = state.error?.toPresentationError(),
        )

        is State.InputSMSCode -> AuthByPhoneViewState.InputSMS(
            phone = state.phone.toE164().value,
            smsCode = state.typedSMSCode.orEmpty(),
            smsCodeMaxLength = state.smsCodeMaxLength,
            resendCodeTimer = state.resendCodeTimer,
            resendEnabled = state.isResendActionAvailable(),
            processing = state.processing,
            action = state.action?.toPresentationAction(),
            error = state.error?.toPresentationError(),
        )
    }


    private fun State.InputPhone.Error.toPresentationError(): AuthByPhoneViewState.InputPhone.Error =
        when (this) {
            State.InputPhone.Error.FailedToSendSms ->
                AuthByPhoneViewState.InputPhone.Error.FailedToSendSMS(
                    messageRes = R.string.auth__auth_by_phone_error_failed_to_send_sms
                )

            State.InputPhone.Error.InvalidPhoneNumber ->
                AuthByPhoneViewState.InputPhone.Error.InvalidPhoneNumber(
                    messageRes = R.string.auth__auth_by_phone_error_invalid_phone_number
                )
        }

    private fun State.InputSMSCode.Action.toPresentationAction(): AuthByPhoneViewState.InputSMS.Action =
        when (this) {
            State.InputSMSCode.Action.AuthorizationSuccess ->
                AuthByPhoneViewState.InputSMS.Action.AuthorizationSuccess

            is State.InputSMSCode.Action.SMSCodeCallback ->
                AuthByPhoneViewState.InputSMS.Action.SMSCodeCallback(
                    smsCode = this.smsCode,
                )

            State.InputSMSCode.Action.RestartResendCodeTimer ->
                AuthByPhoneViewState.InputSMS.Action.RestartResendCodeTimer
        }

    private fun State.InputPhone.Action.toPresentationAction():
        AuthByPhoneViewState.InputPhone.Action = when (this) {
        State.InputPhone.Action.NavigateToTermsOfCondition ->
            AuthByPhoneViewState.InputPhone.Action.NavigateToTermsOfConditions
    }

    private fun State.InputSMSCode.Error.toPresentationError(): AuthByPhoneViewState.InputSMS.Error =
        when (this) {
            State.InputSMSCode.Error.InvalidSMSCodeFormat,
            State.InputSMSCode.Error.Failed,
            ->
                AuthByPhoneViewState.InputSMS.Error.Failed(
                    messageRes = R.string.auth__auth_by_phone_error_incorrect_code_or_else,
                )

            State.InputSMSCode.Error.ResendConfirmationCodeError ->
                AuthByPhoneViewState.InputSMS.Error.Failed(
                    messageRes = R.string.auth__auth_by_phone_error_resend_confirmation_code
                )
        }
}
