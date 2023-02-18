package io.github.ilyapavlovskii.kmm.auth.presentation.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.ilyapavlovskii.kmm.auth.presentation.R
import kotlin.time.Duration

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun AuthByPhoneContentInputSMSView(
    viewState: AuthByPhoneViewState.InputSMS,
    onBackClickListener: () -> Unit,
    onCodeValueListener: (String) -> Unit,
    onResendListener: () -> Unit,
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
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_24),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Back",
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(
                id = R.string.auth__auth_by_phone_title_sms_code,
                formatArgs = arrayOf(viewState.phone)
            ),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                //.let { if (viewState.processing) it.shimmer() else it }
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.auth__auth_by_phone_code_enter_placeholder_char)
                        .repeat(viewState.smsCodeMaxLength.toInt()),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.displayLarge,
                )
            },
            enabled = !viewState.processing,
            maxLines = 1,
            value = viewState.smsCode,
            onValueChange = { value ->
                if (value.length.toUInt() <= viewState.smsCodeMaxLength) onCodeValueListener(value)
            },
            textStyle = MaterialTheme.typography.displayLarge.copy(
                textAlign = TextAlign.Center,
            ),
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                disabledLabelColor = MaterialTheme.colorScheme.secondary,
                disabledTextColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
        )
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = viewState.resendCodeTimer.let {
                if (it == Duration.ZERO) {
                    stringResource(id = R.string.auth__auth_by_phone_confirmation_resend_code_available)
                } else {
                    pluralStringResource(
                        id = R.plurals.auth__auth_by_phone_confirmation_resend_code_pattern,
                        count = it.inWholeSeconds.toInt(),
                        formatArgs = arrayOf(it)
                    )
                }
            },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedButton(
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Transparent,
                containerColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(32.dp),
            enabled = viewState.resendEnabled,
            onClick = onResendListener
        ) {
            Text(
                text = stringResource(id = R.string.auth__resend_code_button_text),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
