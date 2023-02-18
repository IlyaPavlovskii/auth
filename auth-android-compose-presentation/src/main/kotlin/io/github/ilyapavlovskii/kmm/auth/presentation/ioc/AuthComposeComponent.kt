package io.github.ilyapavlovskii.kmm.auth.presentation.ioc

import io.github.ilyapavlovskii.kmm.auth.domain.mvi.email.AuthorizationEmailRedux
import io.github.ilyapavlovskii.kmm.auth.domain.mvi.phone.AuthorizationPhoneRedux
import io.github.ilyapavlovskii.kmm.auth.presentation.email.AuthByEmailViewModel
import io.github.ilyapavlovskii.kmm.auth.presentation.email.AuthByEmailViewState
import io.github.ilyapavlovskii.kmm.auth.presentation.email.AuthByEmailViewStateConverter
import io.github.ilyapavlovskii.kmm.auth.presentation.entry.AuthScreenViewModel
import io.github.ilyapavlovskii.kmm.auth.presentation.phone.AuthByPhoneViewModel
import io.github.ilyapavlovskii.kmm.auth.presentation.phone.AuthByPhoneViewState
import io.github.ilyapavlovskii.kmm.auth.presentation.phone.AuthByPhoneViewStateConverter
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
        factory<ViewStateConverter<AuthorizationPhoneRedux.State, AuthByPhoneViewState>>(
            named<AuthorizationPhoneRedux>()
        ) {
            AuthByPhoneViewStateConverter(smsCodeMaxLengthProvider = get())
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
        viewModel {
            AuthByPhoneViewModel(
                reducer = get(named<AuthorizationPhoneRedux>()),
                effectHandler = get(named<AuthorizationPhoneRedux>()),
                viewStateConverter = get(named<AuthorizationPhoneRedux>()),
                countDownTimerUseCase = get(),
            )
        }
        viewModel { AuthScreenViewModel() }
    }
}