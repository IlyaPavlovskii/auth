package io.github.ilyapavlovskii.kmm.signin.domain.ioc

import io.github.ilyapavlovskii.kmm.signin.domain.usecase.GetAuthorizationStateUseCase
import io.github.ilyapavlovskii.kmm.signin.domain.usecase.email.ResetPasswordByEmailUseCase
import io.github.ilyapavlovskii.kmm.signin.domain.usecase.email.SignByEmailUseCase
import io.github.ilyapavlovskii.kmm.signin.domain.usecase.email.firebase.FirebaseResetPasswordByEmailUseCase
import io.github.ilyapavlovskii.kmm.signin.domain.usecase.email.firebase.FirebaseSignByEmailUseCase
import io.github.ilyapavlovskii.kmm.signin.domain.usecase.firebase.FirebaseGetAuthorizationStateUseCase
import org.koin.dsl.module

internal fun multiplatformUseCaseModule() = module {
    factory<GetAuthorizationStateUseCase> {
        FirebaseGetAuthorizationStateUseCase(firebaseAuth = get())
    }
    factory<ResetPasswordByEmailUseCase> {
        FirebaseResetPasswordByEmailUseCase(firebaseAuth = get())
    }
    factory<SignByEmailUseCase> {
        FirebaseSignByEmailUseCase(firebaseAuth = get())
    }
}