package io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone

import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux.Effect
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux.Message
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux.State
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone.SMSCodeMaxLengthProvider
import io.github.ilyapavlovskii.kmm.common.domain.model.Phone
import net.humans.kmm.mvi.ComplexReducer
import net.humans.kmm.mvi.Return
import net.humans.kmm.mvi.pure
import net.humans.kmm.mvi.withEffect
import kotlin.time.Duration

internal class AuthorizationPhoneReducer(
    private val smsCodeMaxLengthProvider: SMSCodeMaxLengthProvider,
) : ComplexReducer<State, Message, Effect> {

    override fun invoke(state: State, msg: Message): Return<State, Effect> {
        return when (msg) {
            Message.ConfirmSMSCode -> when (state) {
                is State.InputPhone -> state.pure()
                is State.InputSMSCode -> state.withEffect(Effect.ConfirmSMSCode(smsCode = state.typedSMSCode))
            }

            Message.SMSSuccessfullySent -> when (state) {
                is State.InputPhone -> State.InputSMSCode(
                    phone = Phone.MSISDN("${state.typedPhoneValue}"),
                    typedSMSCode = null,
                    processing = false,
                    smsCodeMaxLength = smsCodeMaxLengthProvider.getSMSMaxCodeLength(),
                    resendCodeTimer = Duration.ZERO,
                    action = null,
                    error = null,
                ).pure()

                is State.InputSMSCode -> state.copy(
                    processing = false,
                    action = State.InputSMSCode.Action.RestartResendCodeTimer
                ).pure()
            }

            is Message.SendSMSToPhoneNumber -> when (state) {
                is State.InputPhone -> if (state.isContinuedAvailable()) {
                    state.copy(processing = true).withEffect(
                        Effect.SendSMSToPhoneNumber(
                            phoneNumber = "${state.typedPhoneValue}",
                            authByPhoneWrapper = msg.authByPhoneWrapper,
                        )
                    )
                } else {
                    state.pure()
                }

                is State.InputSMSCode -> state.copy(
                    processing = true,
                    typedSMSCode = null,
                ).withEffect(
                    Effect.SendSMSToPhoneNumber(
                        phoneNumber = state.phone.toE164().value,
                        authByPhoneWrapper = msg.authByPhoneWrapper,
                    )
                )
            }

            is Message.UpdateTypedPhoneNumber -> when (state) {
                is State.InputPhone -> state.copy(
                    typedPhoneValue = msg.phoneNumber,
                ).pure()

                is State.InputSMSCode -> state.pure()
            }

            is Message.UpdateTypedSMSCode -> when (state) {
                is State.InputPhone -> state.pure()
                is State.InputSMSCode -> if (msg.smsCode.length.toUInt() == state.smsCodeMaxLength) {
                    state.copy(
                        processing = true,
                        typedSMSCode = msg.smsCode,
                    ).withEffect(Effect.ConfirmSMSCode(msg.smsCode))
                } else {
                    state.copy(
                        typedSMSCode = msg.smsCode,
                    ).pure()
                }
            }

            Message.ActionHandled -> when (state) {
                is State.InputPhone -> state.copy(action = null).pure()
                is State.InputSMSCode -> state.copy(action = null).pure()
            }

            Message.ErrorHandled -> when (state) {
                is State.InputPhone -> state.copy(error = null).pure()
                is State.InputSMSCode -> state.copy(error = null).pure()
            }

            is Message.AuthByPhonePlatformWrapperSMSCodeCallback -> when (state) {
                is State.InputPhone -> state.pure()

                is State.InputSMSCode -> state.copy(
                    action = State.InputSMSCode.Action.SMSCodeCallback(msg.smsCode)
                ).pure()
            }

            Message.ApiNotAvailableHandled,
            Message.AuthExceptionHandled,
            Message.OtherError,
            -> when (state) {
                is State.InputPhone -> state.copy(
                    error = State.InputPhone.Error.FailedToSendSms,
                    processing = false,
                ).pure()

                is State.InputSMSCode -> state.copy(
                    processing = false,
                    error = State.InputSMSCode.Error.Failed
                ).pure()
            }

            Message.InvalidPhoneNumberHandled -> when (state) {
                is State.InputPhone -> state.copy(
                    error = State.InputPhone.Error.InvalidPhoneNumber,
                    processing = false,
                ).pure()

                is State.InputSMSCode -> state.copy(
                    processing = false,
                    error = State.InputSMSCode.Error.Failed,
                ).pure()
            }

            Message.InvalidSMSCodeFormatHandled -> when (state) {
                is State.InputPhone -> state.pure()

                is State.InputSMSCode -> state.copy(
                    error = State.InputSMSCode.Error.InvalidSMSCodeFormat
                ).pure()
            }

            is Message.ResendConfirmationCode -> when (state) {
                is State.InputPhone -> state.pure()
                is State.InputSMSCode -> state.withEffect(
                    Effect.SendSMSToPhoneNumber(
                        phoneNumber = state.phone.toE164().value,
                        authByPhoneWrapper = msg.authByPhoneWrapper,
                    )
                )
            }

            Message.ResendConfirmationCodeError -> when (state) {
                is State.InputPhone -> state.pure()
                is State.InputSMSCode -> state.copy(
                    error = State.InputSMSCode.Error.ResendConfirmationCodeError
                ).pure()
            }

            is Message.UpdateTimerValue -> when (state) {
                is State.InputPhone -> state.pure()
                is State.InputSMSCode -> state.copy(
                    resendCodeTimer = msg.resendCodeTimer,
                ).pure()
            }

            is Message.UpdateTermsOfConditionState -> when (state) {
                is State.InputPhone -> state.copy(
                    termsOfConditionChecked = msg.checked,
                ).pure()

                is State.InputSMSCode -> state.pure()
            }

            Message.InputPhoneNumberProcessed -> when (state) {
                is State.InputPhone -> state.pure()

                is State.InputSMSCode -> state.copy(
                    processing = false,
                    action = State.InputSMSCode.Action.AuthorizationSuccess,
                ).pure()
            }
        }
    }
}