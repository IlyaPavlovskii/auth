package io.github.ilyapavlovskii.kmm.common.domain.ioc

import io.github.ilyapavlovskii.kmm.koin.Component
import org.koin.core.module.Module

object CommonMultiplatformDomainComponent : Component {
    override val modules: List<Module> = listOf(
        useCaseModule(),
    )
}