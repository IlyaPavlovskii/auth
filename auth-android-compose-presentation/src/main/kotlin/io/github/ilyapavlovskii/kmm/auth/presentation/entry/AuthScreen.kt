package io.github.ilyapavlovskii.kmm.auth.presentation.entry

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel

@Composable
fun AuthScreen(
    navController: NavController,
) {
    val viewModel: AuthScreenViewModel = getViewModel()
    val viewState: AuthScreenViewState by viewModel.viewState.collectAsState()

    AuthContentView(
        viewState = viewState,
        onButtonCLick = { authActionType ->
            navController.navigate(authActionType.route)
        }
    )
}
