package io.github.ilyapavlovskii.kmm.auth.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import io.github.ilyapavlovskii.kmm.auth.presentation.navigation.AuthNavigationGraph
import io.github.ilyapavlovskii.kmm.auth.ui.AuthTheme

internal class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(top = 0.dp),
                ) { innerPadding ->
                    AuthNavigationGraph(
                        navController = navController,
                        authSuccess = {
                            Log.d("MainActivity", "AuthSuccess")
                        },
                        navigateToTermsOfConditions = {
                            Log.d("MainActivity", "navigateToTermsOfConditions")
                        },
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.surface)
                    )
                }
            }
        }
    }
}
