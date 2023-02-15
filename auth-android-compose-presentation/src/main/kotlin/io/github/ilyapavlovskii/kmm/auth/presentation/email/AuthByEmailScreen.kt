package io.github.ilyapavlovskii.kmm.auth.presentation.email

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import io.github.ilyapavlovskii.kmm.auth.presentation.ui.AlertDialog
import org.koin.androidx.compose.getViewModel

@Composable
fun AuthByEmailScreen(
    onBackClick: () -> Unit,
    authSuccess: () -> Unit,
    navigateToTermsOfConditions: () -> Unit,
) {
    val viewModel: AuthByEmailViewModel = getViewModel()
    val viewState = viewModel.viewState.collectAsState()

    AuthByEmailView(
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
    viewState.value.action?.also { action ->
        when (action) {
            AuthByEmailViewState.Action.AuthSuccess -> authSuccess()
            is AuthByEmailViewState.Action.Dialog ->
                AlertDialog(
                    messageRes = action.messageRes,
                    arguments = action.arguments,
                    onCancel = viewModel::actionWithErrorHandled,
                )
            AuthByEmailViewState.Action.NavigateToTermsOfConditions ->
                navigateToTermsOfConditions()
        }
    }
}