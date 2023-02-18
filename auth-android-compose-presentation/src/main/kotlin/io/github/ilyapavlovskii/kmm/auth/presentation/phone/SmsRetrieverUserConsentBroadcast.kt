package io.github.ilyapavlovskii.kmm.auth.presentation.phone

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import io.github.ilyapavlovskii.kmm.common.domain.model.SMSCode

private const val TAG = "SmsRetrieverUserConsentBroadcast"

@Composable
fun SmsRetrieverUserConsentBroadcast(
    smsCodeLength: UInt,
    onSmsReceived: (smsCode: SMSCode) -> Unit,
) {
    val context = LocalContext.current

    var shouldRegisterReceiver by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Log.d(TAG, "Initializing Sms Retriever client: $smsCodeLength")
        SmsRetriever.getClient(context)
            .startSmsUserConsent(null)
            .addOnSuccessListener {
                Log.d(TAG, "SmsRetriever started successfully")
                shouldRegisterReceiver = true
            }
            .addOnCanceledListener {
                Log.d(TAG, "SmsRetriever cancelled")
            }
            .addOnFailureListener {
                Log.d(TAG, "SmsRetriever failed: ${it.message}")
            }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val message: String? = it.data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            Log.d(TAG, "Result OK. Message: $message")
            message?.let {
                Log.d(TAG, "Sms received: $message")
                val verificationCode = getVerificationCodeFromSms(message, smsCodeLength)
                Log.d(TAG, "Verification code parsed: $verificationCode")
                try {
                    val smsCode = SMSCode(verificationCode)
                    onSmsReceived(smsCode)
                } catch (ise: IllegalStateException) {
                    Log.d(TAG, "Error create SMSCode.", ise)
                }
            }
            shouldRegisterReceiver = false
        }
    }

    if (shouldRegisterReceiver) {
        SystemBroadcastReceiver(
            systemAction = SmsRetriever.SMS_RETRIEVED_ACTION,
            broadCastPermission = SmsRetriever.SEND_PERMISSION,
        ) { intent ->
            if (intent != null && SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras
                    ?.getSafeParcelable<Status>(SmsRetriever.EXTRA_STATUS)
                    ?: return@SystemBroadcastReceiver
                Log.d(TAG, "Receive SMS. Status code: $${smsRetrieverStatus.statusCode}")
                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent =
                            extras.getSafeParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            launcher.launch(consentIntent)
                        } catch (e: ActivityNotFoundException) {
                            Log.d(TAG, "Activity Not found for SMS consent API", e)
                        } catch (th: Throwable) {
                            Log.e(TAG, "Exception: ", th)
                        }
                    }

                    CommonStatusCodes.TIMEOUT -> Log.d(TAG, "Timeout in sms verification receiver")
                }
            }
        }
    }
}

private fun getVerificationCodeFromSms(sms: String, smsCodeLength: UInt): String =
    sms.filter { it.isDigit() }
        .substring(0 until smsCodeLength.toInt())
