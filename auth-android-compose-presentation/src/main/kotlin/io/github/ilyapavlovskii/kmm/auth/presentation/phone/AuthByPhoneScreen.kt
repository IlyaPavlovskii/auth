package io.github.ilyapavlovskii.kmm.auth.presentation.phone

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.github.ilyapavlovskii.kmm.auth.presentation.R
import io.github.ilyapavlovskii.kmm.auth.presentation.ui.ErrorDialog
import io.github.ilyapavlovskii.kmm.common.domain.model.SMSCode
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.AndroidAuthByPhonePlatformWrapper
import io.github.ilyapavlovskii.kmm.firebase.auth.model.phone.AuthByPhonePlatformWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import org.koin.androidx.compose.getViewModel
import kotlin.time.Duration.Companion.seconds

private val smsCodeEmitter = MutableStateFlow<SMSCode?>(null)
private val defaultVerificationCodeTimeout = 3.seconds

@Composable
fun AuthByPhoneScreen(
    onBackClick: () -> Unit,
    onAuthorizationSuccess: () -> Unit,
    navigateToTermsOfConditions: () -> Unit,
) {
    val viewModel: AuthByPhoneViewModel = getViewModel()
    val viewState = viewModel.viewState.collectAsState()

    smsCodeEmitter.tryEmit(null)
    val platformWrapper = createAndroidAuthByPhonePlatformWrapper(viewModel::onCodeSent)

    SmsRetrieverUserConsentBroadcast(
        smsCodeLength = when(val vs = viewState.value) {
            is AuthByPhoneViewState.InputPhone -> vs.smsCodeMaxLength
            is AuthByPhoneViewState.InputSMS -> vs.smsCodeMaxLength
        },
    ) { smsCode -> viewModel.updateTypedSmsCode(smsCode.value) }

    when(val value = viewState.value) {
        is AuthByPhoneViewState.InputPhone -> {
            AuthByPhoneContentInputPhoneView(
                viewState = value,
                onBackClickListener = onBackClick,
                onPhoneChanged = viewModel::phoneChanged,
                onContinueClicked = {
                    viewModel.sendSms(authByPhoneWrapper = platformWrapper)
                },
                onTermsOfConditionChange = viewModel::termsOfConditionChanged,
                onTermsOfConditionClicked = viewModel::termsOfConditionSelected,
            )
            value.action?.also { action ->
                when(action) {
                    AuthByPhoneViewState.InputPhone.Action.NavigateToTermsOfConditions -> {
                        viewModel.actionHandled()
                        navigateToTermsOfConditions()
                    }
                }
            }
            value.error?.also { error ->
                when (error) {
                    is AuthByPhoneViewState.InputPhone.Error.FailedToSendSMS ->
                        ErrorDialog(
                            messageRes = error.messageRes,
                            onDismissRequest = viewModel::errorHandled
                        )

                    is AuthByPhoneViewState.InputPhone.Error.InvalidPhoneNumber -> {
                        // DoNothing
                    }
                }
            }
        }

        is AuthByPhoneViewState.InputSMS -> {
            AuthByPhoneContentInputSMSView(
                viewState = value,
                onBackClickListener = onBackClick,
                onCodeValueListener = viewModel::codeValueChanged,
                onResendListener = {
                    viewModel.resendSelected(authByPhoneWrapper = platformWrapper)
                },
            )
            value.action?.also { action ->
                when (action) {
                    AuthByPhoneViewState.InputSMS.Action.AuthorizationSuccess -> {
                        viewModel.actionHandled()
                        onAuthorizationSuccess()
                    }

                    is AuthByPhoneViewState.InputSMS.Action.SMSCodeCallback ->
                        smsCodeEmitter.tryEmit(action.smsCode)

                    AuthByPhoneViewState.InputSMS.Action.RestartResendCodeTimer ->
                        viewModel.restartTimer()
                }
            }
            value.error?.also { error ->
                when(error) {
                    is AuthByPhoneViewState.InputSMS.Error.Failed ->
                        ErrorDialog(
                            messageRes = error.messageRes,
                            dismissTextRes = R.string.auth__auth_by_phone_sms_code_dismiss,
                            onDismissRequest = {
                                viewModel.onErrorHandledAndResendCode(platformWrapper)
                            }
                        )
                }
            }

        }
    }
}

@Composable
private fun createAndroidAuthByPhonePlatformWrapper(
    onCodeSent: () -> Unit,
): AuthByPhonePlatformWrapper = AndroidAuthByPhonePlatformWrapper(
    activity = (LocalContext.current as Activity),
    verificationCodeTimout = defaultVerificationCodeTimeout,
    onCodeSent = onCodeSent,
    smsCodeEmitter = smsCodeEmitter.filterNotNull(),
)