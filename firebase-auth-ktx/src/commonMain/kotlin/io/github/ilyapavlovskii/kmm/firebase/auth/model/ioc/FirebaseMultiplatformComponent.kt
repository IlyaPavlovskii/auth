package io.github.ilyapavlovskii.kmm.firebase.auth.model.ioc

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import io.github.ilyapavlovskii.kmm.koin.Component
import org.koin.core.module.Module
import org.koin.dsl.module

object FirebaseMultiplatformComponent : Component {
    override val modules: List<Module> = listOf(
        module {
            factory<FirebaseAuth> { Firebase.auth }
        }
    )
}