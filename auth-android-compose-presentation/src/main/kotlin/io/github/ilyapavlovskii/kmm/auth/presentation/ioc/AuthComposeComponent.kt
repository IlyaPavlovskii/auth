package io.github.ilyapavlovskii.kmm.auth.presentation.ioc

import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.AuthorizationEmailRedux
import io.github.ilyapavlovskii.kmm.auth.presentation.email.AuthByEmailViewModel
import io.github.ilyapavlovskii.kmm.auth.presentation.email.AuthByEmailViewState
import io.github.ilyapavlovskii.kmm.auth.presentation.email.AuthByEmailViewStateConverter
import io.github.ilyapavlovskii.kmm.auth.presentation.entry.AuthScreenViewModel
import io.github.ilyapavlovskii.kmm.koin.Component
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
        factory<ViewStateConverter<AuthorizationEmailRedux.State, AuthByEmailViewState>>(
            named<AuthorizationEmailRedux>()
        ) {
            AuthByEmailViewStateConverter()
        }
    }

    private fun viewModelModule(): Module = module {
        viewModel {
            AuthByEmailViewModel(
                reducer = get(named<AuthorizationEmailRedux>()),
                effectHandler = get(named<AuthorizationEmailRedux>()),
                viewStateConverter = get(named<AuthorizationEmailRedux>()),
            )
        }
        viewModel { AuthScreenViewModel() }
    }
}