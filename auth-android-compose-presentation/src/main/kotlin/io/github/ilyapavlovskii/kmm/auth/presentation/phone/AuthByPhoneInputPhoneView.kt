package io.github.ilyapavlovskii.kmm.auth.presentation.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import io.github.ilyapavlovskii.kmm.auth.presentation.R
import io.github.ilyapavlovskii.kmm.auth.presentation.utils.authOutlinedTextFieldColors
import io.github.ilyapavlovskii.kmm.auth.ui.AuthTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthByPhoneContentInputPhoneView(
    viewState: AuthByPhoneViewState.InputPhone,
    onBackClickListener: () -> Unit,
    onPhoneChanged: (String) -> Unit,
    onContinueClicked: () -> Unit,
    onTermsOfConditionChange: (Boolean) -> Unit,
    onTermsOfConditionClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(
                            bounded = true,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        onClick = onBackClickListener,
                    )
                    .padding(all = 8.dp),
                imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_back_24),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Back",
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(id = R.string.auth__auth_by_phone_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            value = viewState.phone,
            shape = RoundedCornerShape(16.dp),
            onValueChange = { value ->
                value.takeIf(String::isDigitsOnly)?.also(onPhoneChanged)
            },
            label = { Text(stringResource(id = R.string.auth__auth_by_phone_hint)) },
            placeholder = { Text(stringResource(id = R.string.auth__auth_by_phone_hint)) },
            maxLines = 1,
            enabled = !viewState.isProcessing,
            isError = viewState.error is AuthByPhoneViewState.InputPhone.Error.InvalidPhoneNumber,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = { onContinueClicked() },
            ),
            visualTransformation = PhoneVisualTransformation,
            colors = authOutlinedTextFieldColors(),
        )
        (viewState.error as? AuthByPhoneViewState.InputPhone.Error.InvalidPhoneNumber)?.messageRes
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
                    checkmarkColor = MaterialTheme.colorScheme.background,
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
                text = stringResource(id = R.string.auth__auth_terms_of_conditions),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge.copy(
                    textDecoration = TextDecoration.Underline
                ),
            )
        }

        if (viewState.isProcessing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            enabled = viewState.isContinueAvailable,
            onClick = { onContinueClicked() },
        ) {
            Text(
                text = stringResource(id = R.string.auth__auth_continue_button),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAuthByPhoneContentInputPhoneScreen() {
    AuthTheme {
        AuthByPhoneContentInputPhoneView(
            viewState = AuthByPhoneViewState.InputPhone(
                phone = "299379992",
                termsOfConditionChecked = false,
                isContinueAvailable = false,
                smsCodeMaxLength = 6u,
                isProcessing = false,
            ),
            onBackClickListener = {},
            onPhoneChanged = {},
            onContinueClicked = {},
            onTermsOfConditionClicked = {},
            onTermsOfConditionChange = {},
        )
    }
}