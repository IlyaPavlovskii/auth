package io.github.ilyapavlovskii.kmm.signin.sample

import io.github.ilyapavlovskii.kmm.koin.Component
import org.koin.core.module.Module

internal object SignInComponent : Component {
    override val modules: List<Module> = components()
        .flatMap(Component::modules)

    private fun components(): List<Component> = listOf(
    )
}