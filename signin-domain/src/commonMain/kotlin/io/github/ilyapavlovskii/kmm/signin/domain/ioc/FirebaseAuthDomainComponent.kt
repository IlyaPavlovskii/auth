package io.github.ilyapavlovskii.kmm.signin.domain.ioc

import io.github.ilyapavlovskii.kmm.koin.Component
import org.koin.core.module.Module

object FirebaseAuthDomainComponent : Component {
    override val modules: List<Module> = listOf(
        multiplatformUseCaseModule(),
        providerModule(),
        mviModule(),
    )
}