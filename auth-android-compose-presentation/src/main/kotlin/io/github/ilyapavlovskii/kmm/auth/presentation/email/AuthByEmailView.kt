package io.github.ilyapavlovskii.kmm.auth.presentation.email

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.ilyapavlovskii.kmm.auth.presentation.utils.signInOutlinedTextFieldColors
import io.github.ilyapavlovskii.kmm.auth.ui.AuthTheme
import io.github.ilyapavlovskii.kmm.auth.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthByEmailView(
    viewState: AuthByEmailViewState,
    onBackClick: () -> Unit,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onPasswordVisibilityChanged: (visible: Boolean) -> Unit,
    onContinueClicked: () -> Unit,
    onTermsOfConditionChange: (checked: Boolean) -> Unit,
    onTermsOfConditionClicked: () -> Unit,
    onRestorePassword: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        IconButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple(
                        color = MaterialTheme.colorScheme.primary,
                    ),
                    onClick = { },
                ),
            onClick = onBackClick
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_24),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = stringResource(id = R.string.change_theme__back_content_description),
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            text = stringResource(id = R.string.auth__sign_in_with_email),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            value = viewState.email,
            shape = RoundedCornerShape(16.dp),
            onValueChange = { onEmailChanged(it) },
            label = { Text(text = stringResource(R.string.auth__auth_by_email_hint)) },
            placeholder = { Text(text = stringResource(R.string.auth__auth_by_email_hint)) },
            maxLines = 1,
            enabled = true,
            isError = viewState.error is AuthByEmailViewState.Error.Email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onContinueClicked()
                },
            ),
            colors = signInOutlinedTextFieldColors(),
        )
        (viewState.error as? AuthByEmailViewState.Error.Email)?.messageRes
            ?.let { stringResource(id = it) }
            ?.also { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 32.dp)
                )
            }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            value = viewState.password,
            shape = RoundedCornerShape(16.dp),
            onValueChange = { onPasswordChanged(it) },
            label = { Text(stringResource(R.string.auth__auth_by_email_password_hint)) },
            placeholder = { Text(stringResource(R.string.auth__auth_by_email_password_hint)) },
            maxLines = 1,
            enabled = true,
            isError = viewState.error is AuthByEmailViewState.Error.Password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onContinueClicked()
                },
            ),
            visualTransformation = if (viewState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val imageDrawableRes =
                    if (viewState.passwordVisible) R.drawable.baseline_visibility_24
                    else R.drawable.baseline_visibility_off_24
                val description =
                    if (viewState.passwordVisible) "Hide password" else "Show password"

                IconButton(
                    onClick = {
                        onPasswordVisibilityChanged(!viewState.passwordVisible)
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = imageDrawableRes),
                        description
                    )
                }
            },
            colors = signInOutlinedTextFieldColors(),
        )
        (viewState.error as? AuthByEmailViewState.Error.Password)?.messageRes
            ?.let { stringResource(id = it) }
            ?.also { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 32.dp)
                )
            }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = viewState.termsOfConditionChecked,
                onCheckedChange = onTermsOfConditionChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.secondary,
                    checkmarkColor = MaterialTheme.colorScheme.onBackground,
                )
            )
            Text(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(
                            bounded = true,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        onClick = { onTermsOfConditionClicked() },
                    )
                    .padding(8.dp),
                text = stringResource(id = R.string.auth__auth_by_email_in_terms_of_conditions),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge.copy(
                    textDecoration = TextDecoration.Underline,
                ),
            )
        }
        if (viewState.error is AuthByEmailViewState.Error.Password) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Transparent,
                    containerColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(32.dp),
                onClick = onRestorePassword,
            ) {
                Text(
                    text = stringResource(id = R.string.auth__auth_by_email_restore_password_button),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        if (viewState.processing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            enabled = viewState.isContinueActionAvailable,
            onClick = { onContinueClicked() },
        ) {
            Text(
                text = stringResource(id =R.string.auth__auth_by_email_in_continue_button),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAuthByEmailView() {
    AuthTheme(darkTheme = true) {
        AuthByEmailView(
            viewState = AuthByEmailViewState(
                email = "email@mail.com",
                password = "123456",
                passwordVisible = true,
                termsOfConditionChecked = true,
                processing = false,
                isContinueActionAvailable = true,
            ),
            onBackClick = {},
            onEmailChanged = {},
            onPasswordChanged = {},
            onPasswordVisibilityChanged = {},
            onContinueClicked = {},
            onTermsOfConditionChange = {},
            onTermsOfConditionClicked = {},
            onRestorePassword = {},
        )
    }
}
