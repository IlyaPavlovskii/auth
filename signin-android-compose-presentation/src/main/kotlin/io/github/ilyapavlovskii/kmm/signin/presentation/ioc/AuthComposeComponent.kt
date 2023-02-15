package io.github.ilyapavlovskii.kmm.signin.presentation.ioc

import io.github.ilyapavlovskii.kmm.koin.Component
import io.github.ilyapavlovskii.kmm.signin.domain.mvi.email.AuthorizationEmailRedux
import io.github.ilyapavlovskii.kmm.signin.presentation.email.SignInEmailViewModel
import io.github.ilyapavlovskii.kmm.signin.presentation.email.SignInEmailViewState
import io.github.ilyapavlovskii.kmm.signin.presentation.email.SignInEmailViewStateConverter
import net.humans.kmm.mvi.ViewStateConverter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AuthComposeComponent : Component {
    override val modules: List<Module> = listOf(
        viewModelModule(),
        presentationModule(),
    )

    private fun presentationModule(): Module = module {
        factory<ViewStateConverter<AuthorizationEmailRedux.State, SignInEmailViewState>>(
            named<AuthorizationEmailRedux>()
        ) {
            SignInEmailViewStateConverter()
        }
    }

    private fun viewModelModule(): Module = module {
        viewModel {
            SignInEmailViewModel(
                reducer = get(named<AuthorizationEmailRedux>()),
                effectHandler = get(named<AuthorizationEmailRedux>()),
                viewStateConverter = get(named<AuthorizationEmailRedux>()),
            )
        }
    }
}