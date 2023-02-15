package io.github.ilyapavlovskii.kmm.auth.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import io.github.ilyapavlovskii.kmm.auth.presentation.email.AuthByEmailScreen
import io.github.ilyapavlovskii.kmm.auth.ui.AuthTheme

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthByEmailScreen(
                        onBackClick = { finish() },
                        authSuccess = {},
                        navigateToTermsOfConditions = {},
                    )
                }
            }
        }
    }
}
