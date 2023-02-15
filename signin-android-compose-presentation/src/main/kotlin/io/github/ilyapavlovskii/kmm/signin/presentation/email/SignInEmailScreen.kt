package io.github.ilyapavlovskii.kmm.signin.presentation.email

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.getViewModel

@Composable
fun SignInEmailScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: SignInEmailViewModel = getViewModel()
    val viewState = viewModel.viewState.collectAsState()

    SignInEmailView(
        viewState = viewState.value,
        onBackClick = onBackClick,
        onEmailChanged = viewModel::emailChanged,
        onPasswordChanged = viewModel::passwordChanged,
        onPasswordVisibilityChanged = viewModel::passwordVisibilityChanged,
        onContinueClicked = viewModel::authorize,
        onTermsOfConditionChange = viewModel::termsOfConditionChange,
        onTermsOfConditionClicked = viewModel::termsOfConditionSelected,
        onRestorePassword = viewModel::restorePassword,
    )
}