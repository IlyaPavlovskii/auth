package io.github.ilyapavlovskii.kmm.common.domain.ioc

import io.github.ilyapavlovskii.kmm.common.domain.usecase.CountDownTimerUseCase
import io.github.ilyapavlovskii.kmm.common.domain.usecase.DefaultCountDownTimerUseCase
import org.koin.dsl.module

internal fun useCaseModule() = module {
    factory<CountDownTimerUseCase> {
        DefaultCountDownTimerUseCase()
    }
}