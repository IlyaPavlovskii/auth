package net.humans.kmm.mvi.signin.domain.ioc

import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneEffectHandler
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneReducer
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone.AuthByPhoneNumberUseCase
import io.github.ilyapavlovskii.kmm.auth.domain.validator.FirebaseSMSCodeValidator
import io.github.ilyapavlovskii.kmm.auth.domain.validator.PhoneNumberValidator
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.AndroidAuthByPhonePlatformWrapper
import io.github.ilyapavlovskii.kmm.koin.Component
import net.humans.kmm.mvi.ComplexReducer
import net.humans.kmm.mvi.CoroutineEffectHandler
import net.humans.kmm.mvi.signin.domain.usecase.FirebaseAuthByPhoneNumberUseCase
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object FirebaseAuthDomainImplComponent : Component {
    override val modules: List<Module> = listOf(
        mviModule(),
        useCaseModule(),
    )

    private fun useCaseModule(): Module = module {
        factory<AuthByPhoneNumberUseCase<AndroidAuthByPhonePlatformWrapper>>(
            named<AndroidAuthByPhonePlatformWrapper>()
        ) {
            FirebaseAuthByPhoneNumberUseCase(
                phoneAuthProvider = get(),
                firebaseAuth = get(),
            )
        }
    }

    private fun mviModule() = module {
        // region: AuthorizationPhoneRedux
        factory<ComplexReducer<AuthorizationPhoneRedux.State, AuthorizationPhoneRedux.Message, AuthorizationPhoneRedux.Effect>>(
            named<AuthorizationPhoneRedux>()
        ) {
            AuthorizationPhoneReducer(
                smsCodeMaxLengthProvider = get(),
            )
        }
        factory<CoroutineEffectHandler<AuthorizationPhoneRedux.Effect, AuthorizationPhoneRedux.Message>>(
            named<AuthorizationPhoneRedux>()
        ) {
            AuthorizationPhoneEffectHandler<AndroidAuthByPhonePlatformWrapper>(
                authByPhoneNumberUseCase = get(named<AndroidAuthByPhonePlatformWrapper>()),
                smsCodeValidator = FirebaseSMSCodeValidator(),
                phoneNumberValidator = PhoneNumberValidator(),
            )
        }
        // endregion
    }
}