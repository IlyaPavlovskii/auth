package io.github.ilyapavlovskii.kmm.signin.presentation.email

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import io.github.ilyapavlovskii.kmm.signin.presentation.ui.AlertDialog
import org.koin.androidx.compose.getViewModel

@Composable
fun SignInEmailScreen(
    onBackClick: () -> Unit,
    authSuccess: () -> Unit,
    navigateToTermsOfConditions: () -> Unit,
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
    viewState.value.action?.also { action ->
        when (action) {
            SignInEmailViewState.Action.AuthSuccess -> authSuccess()
            is SignInEmailViewState.Action.Dialog ->
                AlertDialog(
                    messageRes = action.messageRes,
                    arguments = action.arguments,
                    onCancel = viewModel::actionWithErrorHandled,
                )
            SignInEmailViewState.Action.NavigateToTermsOfConditions ->
                navigateToTermsOfConditions()
        }
    }
}