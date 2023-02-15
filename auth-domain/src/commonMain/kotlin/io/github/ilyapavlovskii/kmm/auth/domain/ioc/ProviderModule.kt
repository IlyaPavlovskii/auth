package io.github.ilyapavlovskii.kmm.auth.domain.ioc

import io.github.ilyapavlovskii.kmm.auth.domain.usecase.phone.SMSCodeMaxLengthProvider
import org.koin.dsl.module

internal fun providerModule() = module  {
    factory {
        SMSCodeMaxLengthProvider { 6u }
    }
}