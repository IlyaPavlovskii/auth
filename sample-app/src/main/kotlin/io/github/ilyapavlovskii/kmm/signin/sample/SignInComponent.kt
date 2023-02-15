package io.github.ilyapavlovskii.kmm.signin.sample

import io.github.ilyapavlovskii.kmm.firebase.auth.ioc.FirebaseMultiplatformModule
import io.github.ilyapavlovskii.kmm.firebase.auth.model.ioc.FirebaseMultiplatformComponent
import io.github.ilyapavlovskii.kmm.koin.Component
import io.github.ilyapavlovskii.kmm.signin.domain.ioc.FirebaseAuthDomainComponent
import io.github.ilyapavlovskii.kmm.signin.presentation.ioc.AuthComposeComponent
import org.koin.core.module.Module

internal object SignInComponent : Component {
    override val modules: List<Module> = components()
        .flatMap(Component::modules)

    private fun components(): List<Component> = listOf(
        AuthComposeComponent,
        FirebaseMultiplatformComponent,
        FirebaseMultiplatformModule,
        FirebaseAuthDomainComponent,
    )
}