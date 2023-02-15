package io.github.ilyapavlovskii.kmm.auth.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.ilyapavlovskii.kmm.auth.presentation.email.AuthByEmailScreen
import io.github.ilyapavlovskii.kmm.auth.presentation.entry.AuthActionType
import io.github.ilyapavlovskii.kmm.auth.presentation.entry.AuthScreen

val authRootRoute = "auth"

@Composable
fun AuthNavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
    authSuccess: () -> Unit,
    navigateToTermsOfConditions: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = authRootRoute,
        modifier = modifier,
    ) {
        this.addAuthNavigationGraph(
            navController = navController,
            authSuccess = authSuccess,
            navigateToTermsOfConditions = navigateToTermsOfConditions,
        )
    }
}

fun NavGraphBuilder.addAuthNavigationGraph(
    navController: NavHostController,
    authSuccess: () -> Unit,
    navigateToTermsOfConditions: () -> Unit,
) {
    this.composable(authRootRoute) {
        AuthScreen(navController = navController)
    }
    this.composable(AuthActionType.EMAIL.route) {
        AuthByEmailScreen(
            onBackClick = navController::popBackStack,
            authSuccess = authSuccess,
            navigateToTermsOfConditions = navigateToTermsOfConditions,
        )
    }
}