package io.github.ilyapavlovskii.kmm.auth.presentation.entry

import androidx.lifecycle.ViewModel
import io.github.ilyapavlovskii.kmm.auth.presentation.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class AuthScreenViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(
        AuthScreenViewState(
            items = setOf(
                AuthButtonItem(
                    iconRes = R.drawable.baseline_email_24,
                    textRes = R.string.auth__sign_in_with_email,
                    action = AuthActionType.EMAIL,
                ),
                AuthButtonItem(
                    iconRes = R.drawable.baseline_phone_android_24,
                    textRes = R.string.auth__sign_in_with_phone,
                    action = AuthActionType.PHONE,
                ),
            )
        )
    )
    val viewState: StateFlow<AuthScreenViewState> = _viewState
}