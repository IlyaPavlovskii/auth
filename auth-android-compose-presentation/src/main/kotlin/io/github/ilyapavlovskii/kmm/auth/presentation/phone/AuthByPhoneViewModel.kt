package io.github.ilyapavlovskii.kmm.auth.presentation.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux.Effect
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux.Message
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux.State
import io.github.ilyapavlovskii.kmm.common.domain.usecase.CountDownTimerUseCase
import io.github.ilyapavlovskii.kmm.common.domain.usecase.execute
import io.github.ilyapavlovskii.kmm.common.domain.utils.ceil
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.AuthByPhonePlatformWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.humans.kmm.mvi.ComplexReducer
import net.humans.kmm.mvi.CoroutineEffectHandler
import net.humans.kmm.mvi.LaunchReduxEngine
import net.humans.kmm.mvi.ViewStateConverter
import net.humans.kmm.mvi.consume
import net.humans.kmm.mvi.pure
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

internal class AuthByPhoneViewModel(
    reducer: ComplexReducer<State, Message, Effect>,
    effectHandler: CoroutineEffectHandler<Effect, Message>,
    viewStateConverter: ViewStateConverter<State, AuthByPhoneViewState>,
    private val countDownDuration: Duration = 60.seconds,
    private val countDownInterval: Duration = 1.seconds,
    private val countDownTimerUseCase: CountDownTimerUseCase,
) : ViewModel() {
    private val _viewState =
        MutableStateFlow<AuthByPhoneViewState>(AuthByPhoneViewState.InputPhone())
    val viewState: StateFlow<AuthByPhoneViewState> = _viewState
    private val engine = LaunchReduxEngine(
        tag = TAG,
        initial = State.InputPhone().pure(),
        reducer = reducer,
        effectHandler = effectHandler,
    ).also { consume(it, _viewState, viewStateConverter) }

    private var countdownTimerJob: Job? = null

    fun phoneChanged(phone: String) = engine.send(Message.UpdateTypedPhoneNumber(phone))
    fun updateTypedSmsCode(smsCode: String) = engine.send(Message.UpdateTypedSMSCode(smsCode))
    fun termsOfConditionSelected() = engine.send(Message.TermsOfConditionSelected)
    fun codeValueChanged(code: String) = engine.send(Message.UpdateTypedSMSCode(code))
    fun actionHandled() = engine.send(Message.ActionHandled)
    fun errorHandled() = engine.send(Message.ErrorHandled)

    fun sendSms(authByPhoneWrapper: AuthByPhonePlatformWrapper) =
        engine.send(Message.SendSMSToPhoneNumber(authByPhoneWrapper))

    fun termsOfConditionChanged(checked: Boolean) =
        engine.send(Message.UpdateTermsOfConditionState(checked))

    fun resendSelected(authByPhoneWrapper: AuthByPhonePlatformWrapper) =
        engine.send(Message.ResendConfirmationCode(authByPhoneWrapper))

    fun onCodeSent() {
        engine.send(Message.SMSSuccessfullySent)
        countdownTimerJob?.cancel()
        countdownTimerJob = runCountdownTimer()
    }

    fun restartTimer() {
        engine.send(Message.ActionHandled)
        countdownTimerJob?.cancel()
        countdownTimerJob = runCountdownTimer()
    }

    fun onErrorHandledAndResendCode(authByPhonePlatformWrapper: AuthByPhonePlatformWrapper) {
        countdownTimerJob?.cancel()
        countdownTimerJob = runCountdownTimer()
        engine.send(Message.ErrorHandled)
        engine.send(Message.UpdateTypedSMSCode(""))
        engine.send(Message.UpdateTimerValue(resendCodeTimer = Duration.ZERO))
        engine.send(Message.ResendConfirmationCode(authByPhonePlatformWrapper))
    }

    private fun runCountdownTimer(): Job = countDownTimerUseCase.execute(
        duration = countDownDuration,
        interval = countDownInterval,
    ).onEach { duration ->
        engine.send(Message.UpdateTimerValue(resendCodeTimer = duration.ceil(DurationUnit.SECONDS)))
    }.launchIn(viewModelScope)

    companion object {
        private const val TAG = "AuthByPhoneViewModel"
    }
}