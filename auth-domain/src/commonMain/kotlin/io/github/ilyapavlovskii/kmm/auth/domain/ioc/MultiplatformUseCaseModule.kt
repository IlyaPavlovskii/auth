package io.github.ilyapavlovskii.kmm.auth.domain.ioc

import io.github.ilyapavlovskii.kmm.auth.domain.usecase.GetAuthorizationStateUseCase
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.ResetPasswordByEmailUseCase
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.AuthByEmailUseCase
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.firebase.FirebaseResetPasswordByEmailUseCase
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.email.firebase.FirebaseAuthByEmailUseCase
import io.github.ilyapavlovskii.kmm.auth.domain.usecase.firebase.FirebaseGetAuthorizationStateUseCase
import org.koin.dsl.module

internal fun multiplatformUseCaseModule() = module {
    factory<GetAuthorizationStateUseCase> {
        FirebaseGetAuthorizationStateUseCase(firebaseAuth = get())
    }
    factory<ResetPasswordByEmailUseCase> {
        FirebaseResetPasswordByEmailUseCase(firebaseAuth = get())
    }
    factory<AuthByEmailUseCase> {
        FirebaseAuthByEmailUseCase(firebaseAuth = get())
    }
}