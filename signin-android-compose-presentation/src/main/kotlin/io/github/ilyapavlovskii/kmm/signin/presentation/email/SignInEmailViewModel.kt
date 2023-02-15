package io.github.ilyapavlovskii.kmm.signin.presentation.email

import androidx.lifecycle.ViewModel
import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailRedux.Effect
import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailRedux.Message
import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailRedux.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.humans.kmm.mvi.ComplexReducer
import net.humans.kmm.mvi.CoroutineEffectHandler
import net.humans.kmm.mvi.LaunchReduxEngine
import net.humans.kmm.mvi.ViewStateConverter
import net.humans.kmm.mvi.consume
import net.humans.kmm.mvi.pure

internal class SignInEmailViewModel(
    reducer: ComplexReducer<State, Message, Effect>,
    effectHandler: CoroutineEffectHandler<Effect, Message>,
    viewStateConverter: ViewStateConverter<State, SignInEmailViewState>,
) : ViewModel() {
    private val _viewState = MutableStateFlow(SignInEmailViewState.DEFAULT)
    val viewState: StateFlow<SignInEmailViewState> = _viewState

    private val engine = LaunchReduxEngine(
        tag = TAG,
        initial = State().pure(),
        reducer = reducer,
        effectHandler = effectHandler,
    ).also { consume(it, _viewState, viewStateConverter) }

    fun emailChanged(email: String) = engine.send(Message.UpdateTypedEmailValue(email))
    fun passwordChanged(password: String) = engine.send(Message.UpdateTypedPasswordValue(password))
    fun authorize() = engine.send(Message.Authorize)
    fun termsOfConditionSelected() = engine.send(Message.SelectTermsOfConditions)
    fun actionWithErrorHandled() {
        engine.send(Message.ActionHandled)
        engine.send(Message.ErrorHandled)
    }
    fun restorePassword() = engine.send(Message.SendRestorePassword)
    fun passwordVisibilityChanged(visible: Boolean) =
        engine.send(Message.UpdatePasswordVisibilityValue(visible))

    fun termsOfConditionChange(checked: Boolean) =
        engine.send(Message.UpdateTermsOfConditionValue(checked))

    companion object {
        private const val TAG = "SignInEmailViewModel"
    }
}