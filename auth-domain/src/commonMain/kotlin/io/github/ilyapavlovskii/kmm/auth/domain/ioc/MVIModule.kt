package io.github.ilyapavlovskii.kmm.auth.domain.ioc

import com.chrynan.validator.EmailValidator
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.AuthorizationEmailEffectHandler
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.AuthorizationEmailReducer
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.AuthorizationEmailRedux
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.ResetPasswordEffectHandler
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.SendAuthRequestEffectHandler
import io.github.ilyapavlovskii.kmm.auth.domain.validator.PasswordValidator
import net.humans.kmm.mvi.ComplexReducer
import net.humans.kmm.mvi.CoroutineEffectHandler
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun mviModule() = module {

    // region: AuthorizationEmailRedux
    factory<ComplexReducer<AuthorizationEmailRedux.State, AuthorizationEmailRedux.Message, AuthorizationEmailRedux.Effect>>(
        named<AuthorizationEmailRedux>()
    ) {
        AuthorizationEmailReducer()
    }
    factory<CoroutineEffectHandler<AuthorizationEmailRedux.Effect, AuthorizationEmailRedux.Message>>(
        named<AuthorizationEmailRedux>()
    ) {
        AuthorizationEmailEffectHandler(
            resetPasswordEffectHandler = get(named<AuthorizationEmailRedux.Effect.ResetPassword>()),
            sendAuthRequestEffectHandler = get(named<AuthorizationEmailRedux.Effect.SendAuthorizationRequest>()),
        )
    }
    factory<CoroutineEffectHandler<AuthorizationEmailRedux.Effect.ResetPassword, AuthorizationEmailRedux.Message>>(
        named<AuthorizationEmailRedux.Effect.ResetPassword>()
    ) {
        ResetPasswordEffectHandler(resetPasswordByEmailUseCase = get())
    }
    factory<CoroutineEffectHandler<AuthorizationEmailRedux.Effect.SendAuthorizationRequest, AuthorizationEmailRedux.Message>>(
        named<AuthorizationEmailRedux.Effect.SendAuthorizationRequest>()
    ) {
        SendAuthRequestEffectHandler(
            authByEmailUseCase = get(),
            emailValidator = EmailValidator(),
            passwordValidator = PasswordValidator(),
        )
    }
    // endregion
}