package io.github.ilyapavlovskii.kmm.auth.sample

import io.github.ilyapavlovskii.kmm.firebase.auth.ioc.FirebaseMultiplatformModule
import io.github.ilyapavlovskii.kmm.firebase.auth.model.ioc.FirebaseMultiplatformComponent
import io.github.ilyapavlovskii.kmm.koin.Component
import io.github.ilyapavlovskii.kmm.auth.domain.ioc.FirebaseAuthDomainComponent
import io.github.ilyapavlovskii.kmm.auth.presentation.ioc.AuthComposeComponent
import io.github.ilyapavlovskii.kmm.common.domain.ioc.CommonMultiplatformDomainComponent
import net.humans.kmm.mvi.signin.domain.ioc.FirebaseAuthDomainImplComponent
import org.koin.core.module.Module

internal object AuthComponent : Component {
    override val modules: List<Module> = components()
        .flatMap(Component::modules)

    private fun components(): List<Component> = listOf(
        AuthComposeComponent,
        CommonMultiplatformDomainComponent,
        FirebaseMultiplatformComponent,
        FirebaseMultiplatformModule,
        FirebaseAuthDomainComponent,
        FirebaseAuthDomainImplComponent,
    )
}